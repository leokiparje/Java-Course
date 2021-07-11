package hr.fer.oprpp2.chatApp;

public class InMessage extends Message {

	private long messageCounter;
	private String username;
	private String text;
	
	public InMessage(long messageCounter, String username, String text) {
		setMessageType(MessageType.IN);
		this.messageCounter = messageCounter;
		this.username = username;
		this.text = text;
	}

	public long getMessageCounter() {
		return messageCounter;
	}

	public String getUsername() {
		return username;
	}

	public String getText() {
		return text;
	}
}
