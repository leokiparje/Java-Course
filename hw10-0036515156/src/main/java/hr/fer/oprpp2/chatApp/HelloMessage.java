package hr.fer.oprpp2.chatApp;

public class HelloMessage extends Message {

	private String username;
	private long randKey;
	
	public HelloMessage(long messageCounter, String username,long randKey) {
		
		if (messageCounter!=0) throw new RuntimeException("Message counter from hello message must be 0 but i received: "+messageCounter);

		setMessageType(MessageType.HELLO);
		this.username = username;
		this.randKey = randKey;
	}

	public String getUsername() {
		return username;
	}

	public long getRandKey() {
		return randKey;
	}
}
