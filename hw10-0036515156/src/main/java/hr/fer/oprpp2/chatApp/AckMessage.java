package hr.fer.oprpp2.chatApp;

public class AckMessage extends Message {

	private long messageCounter;
	private long uid;
	
	public AckMessage(long messageCounter, long uid) {
		setMessageType(MessageType.ACK);
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
