package hr.fer.oprpp2.chatApp;

public abstract class Message {

	private MessageType messageType;
	
	public MessageType getMessageType() {
		return messageType;
	}
	
	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
	
}
