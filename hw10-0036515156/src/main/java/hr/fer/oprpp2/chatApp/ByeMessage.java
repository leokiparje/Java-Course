package hr.fer.oprpp2.chatApp;

public class ByeMessage extends Message {

	private long messageCounter;
	private long uid;
	
	public ByeMessage(long messageCounter, long uid) {
		setMessageType(MessageType.BYE);
		this.messageCounter = messageCounter;
		this.uid = uid;
	}

	public long getMessageCounter() {
		return messageCounter;
	}

	public long getUid() {
		return uid;
	}
}
