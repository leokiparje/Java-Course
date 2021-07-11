package hr.fer.oprpp2.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class RequestContext represents a request context. It has all the needed information about the request.
 * @author leokiparje
 *
 */

public class RequestContext {
	
	private OutputStream outputStream;
	private Charset charset;
	
	private Map<String,String> parameters;
	private Map<String,String> temporaryParameters;
	private Map<String,String> persistentParameters;
	private List<RCCookie> outputCookies;
	private boolean headerGenerated;
	private IDispatcher dispatcher;
	
	public String encoding;
	public int statusCode;
	public String statusText;
	public String mimeType;
	public Long contentLength;
	
	public RequestContext(OutputStream outputStream,
							Map<String,String> parameters,
							Map<String,String> persistentParameters,
							List<RCCookie> outputCookies) {
		this(outputStream,parameters,persistentParameters,outputCookies,null,null);
		temporaryParameters = new HashMap<>();
	}
	
	public RequestContext(OutputStream outputStream,
						Map<String,String> parameters,
						Map<String,String> persistentParameters,
						List<RCCookie> outputCookies,
						Map<String,String> temporaryParameters,
						IDispatcher dispatcher) {
		this.outputStream = outputStream;
		this.parameters = parameters;
		this.temporaryParameters = new HashMap<>();
		this.persistentParameters = persistentParameters;
		this.outputCookies = outputCookies;
		this.temporaryParameters = temporaryParameters;
		this.dispatcher = dispatcher;
		this.headerGenerated = false;
		this.encoding = "UTF-8";
		this.statusCode = 200;
		this.statusText = "OK";
		this.mimeType = "text/html";
		this.contentLength = null;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	
	public IDispatcher getDispatcher() {
		return this.dispatcher;
	}
	
	public void setContentLength(Long contentLength) {
		this.contentLength = contentLength;
	}
	
	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	public Set<String> getParameterNames(){
		Set<String> result = new HashSet<>();
		for (String s : parameters.keySet()) {
			result.add(s);
		}
		return Collections.unmodifiableSet(result);
	}
	
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}
	
	public Set<String> getPersistentParameterNames(){
		Set<String> result = new HashSet<>();
		for (String s : persistentParameters.keySet()) {
			result.add(s);
		}
		return Collections.unmodifiableSet(result);
	}
	
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name,value);
	}
	
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}
	
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}
	
	public Set<String> getTemporaryParameterNames(){
		Set<String> result = new HashSet<>();
		for (String s : temporaryParameters.keySet()) {
			result.add(s);
		}
		return Collections.unmodifiableSet(result);
	}
	
	public String getSessionID() {
		return "";
	}
	
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name,value);
	}
	
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}
	
	public void setEncoding(String encoding) {
		if (headerGenerated) throw new RuntimeException("Cannot change the value of this field as header was already created.");
		this.encoding = encoding;
	}
	
	public void setMimeType(String mimeType) {
		if (headerGenerated) throw new RuntimeException("Cannot change the value of this field as header was already created.");
		this.mimeType = mimeType;
	}
	
	public void setStatusCode(Integer statusCode) {
		if (headerGenerated) throw new RuntimeException("Cannot change the value of this field as header was already created.");
		this.statusCode = statusCode;
	}
	
	public void setStatusText(String statusText) {
		if (headerGenerated) throw new RuntimeException("Cannot change the value of this field as header was already created.");
		this.statusText = statusText;
	}
	
	public void addRCCookie(RCCookie cookie) {
		if (headerGenerated) throw new RuntimeException("Cannot change the value of this field as header was already created.");
		outputCookies.add(cookie);
	}

	public static class RCCookie {
		
		private String name;
		private String value;
		private String domain;
		private String path;
		private Integer maxAge;
		
		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			super();
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}
		
		public String getName() {
			return name;
		}
		public String getValue() {
			return value;
		}
		public String getDomain() {
			return domain;
		}
		public String getPath() {
			return path;
		}
		public Integer getMaxAge() {
			return maxAge;
		}	
	}
	
	public RequestContext write(byte[] data) throws IOException {
		if (!headerGenerated) generateHeader();
		outputStream.write(data);
		return this;
	}
	
	public RequestContext write(byte[] data, int offset, int len) throws IOException {
		if (!headerGenerated) generateHeader();
		outputStream.write(data);
		return this;
	}
	
	public RequestContext write(String text) throws IOException {
		if (!headerGenerated) generateHeader();
		byte[] data = text.getBytes(charset);
		outputStream.write(data);
		return this;
	}
	
	private void generateHeader() throws IOException {
		charset = Charset.forName(encoding);
		String contentType = mimeType.startsWith("text/") ? mimeType+"; charset="+encoding : mimeType;
		String contentLen = contentLength==null ? "" : "Content-Length: "+contentLength+"\r\n";
		StringBuilder cookiesSb = new StringBuilder();
		if (outputCookies!=null) {
			for (RCCookie cookie : outputCookies) {
				cookiesSb.append("Set-Cookie: "+cookie.getName()+"=\""+cookie.getValue()+"\"");
				if (cookie.getDomain()!=null) cookiesSb.append("; Domain="+cookie.getDomain());
				if (cookie.getPath()!=null) cookiesSb.append("; Path="+cookie.getPath());
				if (cookie.getMaxAge()!=null) cookiesSb.append("; Max-Age="+cookie.maxAge);
				cookiesSb.append("\n");
			}
		}
		outputStream.write(
				("HTTP/1.1 "+statusCode+" "+statusText+"\r\n"+
				"Content-Type: "+contentType+"\r\n"+
				contentLen+
				cookiesSb.toString()+
				"\r\n").getBytes(StandardCharsets.ISO_8859_1)
				);
		headerGenerated = true;
	}	
}






























































































