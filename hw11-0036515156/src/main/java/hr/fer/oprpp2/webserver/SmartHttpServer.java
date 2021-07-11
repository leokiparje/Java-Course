package hr.fer.oprpp2.webserver;

import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.oprpp2.scripting.exec.SmartScriptEngine;
import hr.fer.oprpp2.scripting.parser.SmartScriptParser;
import hr.fer.oprpp2.webserver.RequestContext.RCCookie;

/**
 * Class SmartHttpServer represents a custom http server that serves files included in project directory.
 * SmartHttpServer uses cookies to track users. After 10 minutes of user being idle, session is deleted.
 * SmartHttpServer provides lots of different endpoints which client can access.
 * @author leokiparje
 *
 */

public class SmartHttpServer {

	@SuppressWarnings("unused")
	private String address; // never used because we bind server socket to any available address
	private String domainName;
	private int port;
	private int workerThreads;
	private int sessionTimeout;
	private Map<String,String> mimeTypes = new HashMap<String, String>();
	private Map<String,IWebWorker> workersMap = new HashMap<>();
	private Map<String,SessionMapEntry> sessions = new HashMap<>();
	private ServerThread serverThread;
	private ExecutorService threadPool;
	private Path documentRoot;
	private Random sessionRandom = new Random();
	
	public SmartHttpServer(String configFileName) {
		try {
			readFromConfigFiles(configFileName);
		}catch(Exception e) {
			System.out.println("Unable to read from config properties.");
		}
	}
	protected synchronized void start() {
		serverThread = new ServerThread();
		serverThread.start();

		threadPool = Executors.newFixedThreadPool(workerThreads);
		
		Thread daemonic = new Thread( () -> {
			while(true) {
				try {
					Thread.sleep(300000);
				} catch (InterruptedException ignorable) {}
				
				for (Map.Entry<String,SessionMapEntry> entry : sessions.entrySet()) {
					if (entry.getValue().getValidUntil() < System.currentTimeMillis()/1000) {
						sessions.remove(entry.getKey());
					}
				}
			}
		});
		
		daemonic.setDaemon(true);
		daemonic.start();
	}
	@SuppressWarnings("deprecation")
	protected synchronized void stop() {
		serverThread.stop();
		threadPool.shutdown();
	}
	protected class ServerThread extends Thread {
		@SuppressWarnings("resource")
		@Override
		public void run() {
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket();
			} catch (IOException e1) {
				System.out.println("Unable to open server socket.");
				e1.printStackTrace();
			}
			try {
				serverSocket.bind(new InetSocketAddress((InetAddress)null, port));
			} catch (IOException e) {
				System.out.println("Unable to bind server socket to specified port.");
				e.printStackTrace();
			}
			while(true) {
				Socket client = null;
				try {
					client = serverSocket.accept(); // blocking method
				} catch (IOException e) {
					System.out.println("Unable to create new client socket.");
					e.printStackTrace();
				}
				ClientWorker cw = new ClientWorker(client);
				threadPool.submit(cw);
			}
		}
	}
	
	/**
	 * Class ClientWorker is a thread which is being called to execute once client specifies address of our server in browser.
	 * @author leokiparje
	 *
	 */
	
	private class ClientWorker implements Runnable,IDispatcher {
	
		private Socket csocket;
		private InputStream istream;
		private OutputStream ostream;
		private String version;
		private String method;
		private String host;
		private RequestContext context = null;
		
		Map<String,String> params = new HashMap<String, String>();
		Map<String,String> tempParams = new HashMap<String, String>();
		Map<String,String> permParams = new HashMap<String, String>();
		List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		
		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
		}
		@Override
		public void run() {
			try {
				istream = new BufferedInputStream(csocket.getInputStream());
				ostream = new BufferedOutputStream(csocket.getOutputStream());
				
				Optional<byte[]> request = readRequest();
				if (request.isEmpty()) {
					sendEmptyResponse(ostream,400,"Bad request");
					return;
				}
				String requestStr = new String(request.get(),StandardCharsets.US_ASCII);
				
				List<String> headers = extractHeaders(requestStr);
				String[] firstLine = headers.isEmpty() ? null : headers.get(0).split(" ");
				if(firstLine==null || firstLine.length != 3) {
					sendEmptyResponse(ostream, 400, "Bad request");
					return;
				}
				
				method = firstLine[0].toUpperCase();
				if(!method.equals("GET")) {
					sendEmptyResponse(ostream, 405, "Method Not Allowed");
					return;
				}
				version = firstLine[2].toUpperCase();
				if(!(version.equals("HTTP/1.1") || version.equals("HTTP/1.0"))) {
					sendEmptyResponse(ostream, 505, "HTTP Version Not Supported");
					return;
				}
				host = domainName;
				for (String header : headers) {
					if (header.startsWith("Host:")) {
						host = header.split(":")[1].trim();
						if (host.contains(":")) host = host.substring(0,host.indexOf(":"));
						break;
					}
				}
				
				checkSession(headers);

				String[] urlParts = firstLine[1].split("\\?");
				String path = urlParts[0];
				
				if (urlParts.length==2) {
					String paramString = urlParts[1];
					
					String[] paramPairs = paramString.split("&");
					for (String pair : paramPairs) {
						String key = pair.split("=")[0];
						String value = pair.split("=")[1];
						params.put(key, value);
					}
				}

				internalDispatchRequest(path,true);
				
				ostream.flush();
				csocket.close();
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		private void checkSession(List<String> headers) {
			
			String sidCandidate = null;
			
			for (String header : headers) {
				if (header.startsWith("Cookie:")) {
					String cookies = header.substring(8);
					String[] cookiePairs = cookies.split(";");
					for (String cookiePair : cookiePairs) {
						String key = cookiePair.split("=")[0].trim();
						String value = cookiePair.split("=")[1].trim();
						if (key.equals("sid")) {
							sidCandidate = value;
							break;
						}
					}
				}
			}
			if (sidCandidate!=null) {
				SessionMapEntry currentSession = sessions.get(sidCandidate);
				if (currentSession!=null) {
					if (currentSession.getHost().equals(host)) {
						if (System.currentTimeMillis()/1000 > currentSession.getValidUntil()) {
							sessions.remove(sidCandidate);
						}else {
							currentSession.setValidUntil(System.currentTimeMillis()/1000 + sessionTimeout);
							permParams = currentSession.getMap();
							return;
						}
					}
				}
			}else {
				long newSid = sessionRandom.nextLong()&Long.MAX_VALUE;
				sidCandidate = String.valueOf(newSid);
			}
			SessionMapEntry newSession = new SessionMapEntry(host,System.currentTimeMillis()/1000+sessionTimeout,new ConcurrentHashMap<>());
			sessions.put(sidCandidate,newSession);
			outputCookies.add(new RequestContext.RCCookie("sid",sidCandidate,null,host,"/"));
			permParams = newSession.getMap();
		}
		
		private String extractMimeType(Path requestedPath) {
			return extractExtension(requestedPath);
		}
		
		private String extractExtension(Path requestedPath) {
			int index = requestedPath.toString().lastIndexOf(".");
			if (index>0) {
				String extension = requestedPath.toString().substring(index+1);
				String mimeType = mimeTypes.get(extension);
				return mimeType;
			}
			return "application/octet-stream";
		}
		
		private void sendError(int statusCode, String statusText) throws IOException {
			ostream.write(
						("HTTP/1.1 "+statusCode+" "+statusText+"\r\n" +
                        "Server: simple java server\r\n" +
                        "Content-Type: text/plain;charset=UTF-8\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n").getBytes(StandardCharsets.US_ASCII)
					);
		}
		
		private void sendEmptyResponse(OutputStream outputStream, int statusCode, String statusText) throws IOException {
			sendResponseWithData(outputStream, statusCode, statusText, "text/plain;charset=UTF-8", new byte[0]);
		}
		private void sendResponseWithData(OutputStream outputStream, int statusCode, String statusText, String contentType, byte[] data) throws IOException {
			outputStream.write(
					("HTTP/1.1 "+statusCode+" "+statusText+"\r\n"+
							"Server: simple java server\r\n"+
							"Content-Type: "+contentType+"\r\n"+
							"Content-Length: "+data.length+"\r\n"+
							"Connection: close\r\n"+
							"\r\n").getBytes(StandardCharsets.US_ASCII)
					);
			outputStream.write(data);
			outputStream.flush();
		}
		private List<String> extractHeaders(String requestHeader){
			List<String> headers = new ArrayList<String>();
			String currentLine = null;
			for(String s : requestHeader.split("\n")) {
				if(s.isEmpty()) break;
				char c = s.charAt(0);
				if(c==9 || c==32) {
					currentLine += s;
				} else {
					if(currentLine != null) {
						headers.add(currentLine);
					}
					currentLine = s;
				}
			}
			if(!currentLine.isEmpty()) {
				headers.add(currentLine);
			}
			return headers;
		}
		
		private Optional<byte[]> readRequest() throws IOException {
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			return readRequest(bos);
		}
		
		private Optional<byte[]> readRequest(ByteArrayOutputStream bos) throws IOException {
			int state = 0;
line:			while(true) {
				int b = istream.read();
				if (b==-1) {
					if (bos.size()!=0) throw new IOException("Incomplete header received.");
					return Optional.empty();
				}
				if(b!=13) {
					bos.write(b);
				}
				switch(state) {
				case 0: 
					if(b==13) { state=1; } else if(b==10) state=4;
					break;
				case 1: 
					if(b==10) { state=2; } else state=0;
					break;
				case 2: 
					if(b==13) { state=3; } else state=0;
					break;
				case 3: 
					if(b==10) { break line; } else state=0;
					break;
				case 4: 
					if(b==10) { break line; } else state=0;
					break;
				}
			}
			return Optional.of(bos.toByteArray());
		}
		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}
		
		private void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
			
			if (context==null) {
				context = new RequestContext(ostream, params, permParams, outputCookies,tempParams,this);
			}
		
			if (urlPath.startsWith("/private")) {
                if (directCall) {
                    sendError(404, "File not Found");
                    return;
                }

            }
			
			if (urlPath.startsWith("/ext/")) {
				Class<?> classReference = this.getClass().getClassLoader().loadClass("hr.fer.oprpp2.webserver.workers."+urlPath.substring(5));
				@SuppressWarnings("deprecation")
				Object newObject = classReference.newInstance();
				IWebWorker iww = (IWebWorker) newObject;
				iww.processRequest(context);
				return;
			}
			
			if (workersMap.containsKey(urlPath)) {
				
				workersMap.get(urlPath).processRequest(context);
				return;
			}
			
			Path requestedPath = documentRoot.resolve(urlPath.substring(1)).toAbsolutePath();

			if (!requestedPath.startsWith(documentRoot)) {
				try {
					sendError(403,"forbidden");
				}catch(Exception e) {
					System.out.println("Unable to send error 403.");
					e.printStackTrace();
				}
			}
			
			if (!(Files.isRegularFile(requestedPath) && Files.isReadable(requestedPath))) {
				sendError(404,"file not found");
			}
			
			if (urlPath.endsWith(".smscr")) {
				String document = new String(Files.readAllBytes(requestedPath),StandardCharsets.UTF_8);
				new SmartScriptEngine(new SmartScriptParser(document).getDocumentNode(),context).execute();
				return;
			}
			
			String mimeType = extractMimeType(requestedPath);
			context.setMimeType(mimeType);
			
			InputStream inStream = Files.newInputStream(requestedPath);

			context.setContentLength(Files.size(requestedPath));
			
			byte[] buf = new byte[1024];
			while(true) {
				int read = inStream.read(buf);
				if (read < 1) break;
				context.write(Arrays.copyOfRange(buf,0,read));
			}
		}
	}
	
	private static class SessionMapEntry {
		
		String host;
		long validUntil;
		Map<String,String> map; // thread safe map
		
		public SessionMapEntry(String host, long validUntil, Map<String,String> map) {
			this.host = host;
			this.validUntil = validUntil;
			this.map = map;
		}
		
		public Map<String,String> getMap(){
			return map;
		}
		
		public void setValidUntil(long validUntil) {
			this.validUntil = validUntil;
		}
		
		public String getHost() {
			return host;
		}
		
		public long getValidUntil() {
			return validUntil;
		}
	}
	
	private void readFromConfigFiles(String configFileName) throws Exception {
		Properties properties = new Properties();		
		InputStream inputStream = SmartHttpServer.class.getClassLoader().getResourceAsStream(configFileName);
		if (inputStream!=null) {
			properties.load(inputStream);
		}else throw new FileNotFoundException("Property file "+configFileName+" not found in the classpath.");
		
		address = properties.getProperty("server.address");
		domainName = properties.getProperty("server.domainName");
		port = Integer.parseInt(properties.getProperty("server.port"));
		workerThreads = Integer.parseInt(properties.getProperty("server.workerThreads"));
		documentRoot = Paths.get(properties.getProperty("server.documentRoot"));
		sessionTimeout = Integer.parseInt(properties.getProperty("session.timeout"));
		
		configMimeProperties(properties);
		configWorkersProperties(properties);
	}
	
	private void configMimeProperties(Properties properties) throws IOException {
		List<String> mTypes = Files.readAllLines(Paths.get(properties.getProperty("server.mimeConfig")));
		for (String mime : mTypes) {
			String[] mimes = mime.split(" = ");
			mimeTypes.put(mimes[0].trim(),mimes[1].trim());
		}
	}
	
	private void configWorkersProperties(Properties properties) throws Exception {
		List<String> workers = Files.readAllLines(Paths.get(properties.getProperty("server.workers")));
		for (String line : workers) {
			String[] arr = line.split("=");
			String path = arr[0].trim();
			String fqcn = arr[1].trim();
			
			Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
			@SuppressWarnings("deprecation")
			Object newObject = referenceToClass.newInstance();
			
			IWebWorker iww = (IWebWorker)newObject;
			workersMap.put(path, iww);
		}
	}
	
	/*
	 * Main method to start the server.
	 */
	
	public static void main(String[] args) throws IOException {
		new SmartHttpServer("server.properties").start();
	}
}








































