package hr.fer.oprpp2.chatApp;

import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class User {

	private String username;
	private long randKey;
	private long uid;
	
	private InetAddress address;
	private int port;
	
	private long messageCounter;
	
	private BlockingQueue<Message> receivedMessages;
	private BlockingQueue<Message> sendingMessages;
	
	private ServerSideClientThread thread;
	
	public User(InetAddress address, int port, long randKey, long uid, String username) {
		this.address = address;
		this.port = port;
		this.randKey = randKey;
		this.uid = uid;
		this.username = username;
		
		messageCounter = 0;
		
		receivedMessages = new LinkedBlockingQueue<>();
		sendingMessages = new LinkedBlockingQueue<>();
	}
	
	public long getRandKey() {
		return randKey;
	}
	
	public ServerSideClientThread getThread() {
		return thread;
	}
	
	public void setThread(ServerSideClientThread thread) {
		this.thread = thread;
	}

	public BlockingQueue<Message> getReceivedMessages() {
		return receivedMessages;
	}

	public BlockingQueue<Message> getSendingMessages() {
		return sendingMessages;
	}
	
	public long getUid() {
		return uid;
	}
	
	public InetAddress getAddress() {
		return address;
	}
	
	public int getPort() {
		return port;
	}
	
	public long getMessageCounter() {
		return messageCounter;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void incrementMessageCounter() {
		this.messageCounter++;
	}
	
	public void decrementMessageCounter() {
		this.messageCounter--;
	}
}
