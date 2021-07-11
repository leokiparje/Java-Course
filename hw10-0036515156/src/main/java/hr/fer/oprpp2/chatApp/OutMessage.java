package hr.fer.oprpp2.chatApp;

public class OutMessage extends Message {

	private long messageCounter;
	private long uid;
	private String text;
	
	public OutMessage(long messageCounter, long uid, String text) {
		setMessageType(MessageType.OUT);
		this.messageCounter = messageCounter;
		this.uid = uid;
		this.text = text;
	}

	public long getMessageCounter() {
		return messageCounter;
	}

	public long getUid() {
		return uid;
	}

	public String getText() {
		return text;
	}
}
