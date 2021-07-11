package hr.fer.oprpp2.chatApp;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.TimeUnit;

public class ServerSideClientThread extends Thread {

	private User user;
	private DatagramSocket serverSocket;
	
	private boolean isRunning;
	
	public ServerSideClientThread(User user, DatagramSocket serverSocket) {
		this.user = user;
		this.serverSocket = serverSocket;
		
		isRunning = true;
	}
	
	public void run() {
		
		while(true) {
			
			try {
				Message message = user.getSendingMessages().poll(5, TimeUnit.SECONDS);
				if (!isRunning) return;
				if (message==null) continue;
				sendMessageToClient(message);
			}catch(Exception e) {}
			
		}
		
	}
	
	private void sendMessageToClient(Message message) {
			
		switch(message.getMessageType()) {
		case ACK:
			sendAckToClient((AckMessage)message);
		case IN:
			sendInToClient((InMessage)message);
		default:
			System.out.println("Invalid state.");
		}
		
	}
	
	private void sendAckToClient(AckMessage ackMessage) {
		
		byte[] buf = fillTheBuffer(ackMessage);
		
		DatagramPacket packet = new DatagramPacket(buf,buf.length,user.getAddress(),user.getPort());
		
		int failCounter = 0;
		
		while(failCounter < 10) {
			
			failCounter++;
			try {
				serverSocket.send(packet);
			}catch(Exception e) {
				System.out.println("Unable to send ack message from server.");
			}
			
			Message message = null;
			
			try {
				message = user.getReceivedMessages().poll(5, TimeUnit.SECONDS);
			}catch(Exception e) {
				continue;
			}
			
			if (message!=null && message.getMessageType().equals(MessageType.ACK)) {
				AckMessage ackMsg = (AckMessage)message;
				if (ackMsg.getMessageCounter() != ackMessage.getMessageCounter()) {
					System.out.println("Message counter of ack msg is not valid.");
					continue;
				}else {
					break;
				}
			}else {
				System.out.println("Invalid message in queue of received messages.");
			}
		}
	}

	private void sendInToClient(InMessage inMessage) {
		
		byte[] buf = fillTheBuffer(inMessage);
		
		DatagramPacket packet = new DatagramPacket(buf,buf.length,user.getAddress(),user.getPort());
		
		int failCounter = 0;
		
		while(failCounter < 10) {
			
			failCounter++;
			try {
				serverSocket.send(packet);
			}catch(Exception e) {
				System.out.println("Unable to send ack message from server.");
			}
			
			Message message = null;
			
			try {
				message = user.getReceivedMessages().poll(5, TimeUnit.SECONDS);
			}catch(Exception e) {
				continue;
			}
			
			if (message!=null && message.getMessageType().equals(MessageType.ACK)) {
				InMessage inMsg = (InMessage)message;
				if (inMsg.getMessageCounter() != inMessage.getMessageCounter()) {
					System.out.println("Message counter of in msg is not valid.");
					continue;
				}else {
					break;
				}
			}else {
				System.out.println("Invalid message in queue of received messages.");
			}
		}
		
	}
	
	private byte[] fillTheBuffer(AckMessage ackMessage) {
	
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		try {
			DataOutputStream dos = new DataOutputStream(bos);
			
			dos.write(2);
			dos.writeLong(ackMessage.getMessageCounter());
			dos.writeLong(user.getUid());
			
			dos.close();
			
		}catch(Exception e) {}
		
		return bos.toByteArray();
	
	}
	
	private byte[] fillTheBuffer(InMessage inMessage) {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		try {
			DataOutputStream dos = new DataOutputStream(bos);
			
			dos.write(5);
			dos.writeLong(inMessage.getMessageCounter());
			dos.writeUTF(inMessage.getUsername());
			dos.writeUTF(inMessage.getText());
			
			dos.close();
			
		}catch(Exception e) {}
		
		return bos.toByteArray();
		
	}
	
	public void killThread() {
		isRunning = false;
	}
	
}
