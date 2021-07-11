package hr.fer.oprpp2.chatApp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class Server represents a server in UDP protocol communication. Server is listening on a port and receives messages from any user connected
 * to it. Once the message is received server sends back appropriate message. In case of a regular message, server sends that text message to
 * all the clients on the network, including the one who sent the message.
 * @author leokiparje
 *
 */

public class Server {

	private DatagramSocket socket;
	private int port;
	
	private Random rand;
	private Long currentUid;
	
	private List<User> users;
	
	public Server(DatagramSocket socket,int port) {
		this.socket = socket;
		this.port = port;
		
		users = new ArrayList<>();
		
		rand = new Random();
		currentUid = rand.nextLong() & Long.MAX_VALUE;
	}
	
	public static void main(String[] args) {
		
		if (args.length!=1) {
			System.out.println("Dragi korisniče očekivao sam jedan argument, port.");
			return;
		}
		
		int port = -1;
		
		try {
			port = Integer.parseInt(args[0]);
		}catch(Exception e) {
			System.out.println("Unable to parse the port number from command arguments.");
		}
		
		DatagramSocket socket = null;
		try{
			socket = new DatagramSocket(port);
		}catch(Exception e) {
			System.out.println("Unable to open new socket.");
		}
		
		Server server = new Server(socket,port);
		server.run();
	}
	
	private void run() {
		
		while(true) {
			
			byte[] buf = new byte[4000];
			DatagramPacket packet = new DatagramPacket(buf,buf.length);
			
			try {
				socket.receive(packet);
			}catch(Exception e) {
				System.out.println("Unable to receive packet. Unexpected error");
				continue;
			}
			
			Message message = null;
			try {
				message = getMessageFromPacket(packet.getData(),packet.getOffset(),packet.getLength());
			}catch(Exception e){
				System.out.println("Unable to get message from received packet on server side.");
			}

			switch(message.getMessageType()) {
			case HELLO:
				processHelloMessage(packet,(HelloMessage)message);
				break;
			case ACK:
				processAckMessage(packet,(AckMessage)message,buf);
				break;
			case BYE:
				processByeMessage(packet,(ByeMessage)message);
				break;
			case IN:
				break;
			case OUT:
				processOutMessage(packet,(OutMessage)message);
				break;
			}
			
		}
		
	}
	
	private void processHelloMessage(DatagramPacket packet, HelloMessage helloMessage) {
		
		User user1 = null;
		AckMessage ackMessage = null;
		
		for (User u : users) {
			if (u.getRandKey()==helloMessage.getRandKey()) {
				user1 = u;
				ackMessage = new AckMessage(0,u.getUid());
				byte[] buf = fillTheBuffer(ackMessage);
				DatagramPacket sendingPacket = new DatagramPacket(buf,buf.length,u.getAddress(),u.getPort());
				try {
					socket.send(sendingPacket);
				} catch (IOException e) { System.out.println("Unable to send ack message to hello."); }
				return;
			}
		}
		
		final User user2 = new User(packet.getAddress(),packet.getPort(),helloMessage.getRandKey(),currentUid++,helloMessage.getUsername());
		user2.setThread(
			new ServerSideClientThread(user2,socket)
		);
		user2.getThread().start();
		users.add(user2);
		ackMessage = new AckMessage(0,user2.getUid());
		
		user2.incrementMessageCounter(); // Increment message counter for client on server when ack is being sent
		
		byte[] buf = fillTheBuffer(ackMessage);
		DatagramPacket sendingPacket = new DatagramPacket(buf,buf.length,user2.getAddress(),user2.getPort());
		try {
			socket.send(sendingPacket);
		} catch (IOException e) { System.out.println("Unable to send ack message to hello."); }

	}
	
	private void processAckMessage(DatagramPacket packet, AckMessage ackMessage, byte[] buf) {
		
		User user = findUserWithUid(ackMessage.getUid());
		
		if (user==null) return;
		
		user.getReceivedMessages().add(ackMessage);
	}
	
	private void processByeMessage(DatagramPacket packet, ByeMessage byeMessage) {
		
		User user = findUserWithUid(byeMessage.getUid());
		
		if (user==null) return;
		
		if (byeMessage.getMessageCounter() != user.getMessageCounter()) {
			System.out.println("Packet with bye message has unexpected message counter: "+byeMessage.getMessageCounter()+", was expecting: "+user.getMessageCounter());
		}
		
		AckMessage ackMessage = new AckMessage(byeMessage.getMessageCounter(),byeMessage.getUid());
		byte[] buf = fillTheBuffer(ackMessage);
		
		DatagramPacket packetAck = new DatagramPacket(buf,buf.length,user.getAddress(),user.getPort());
		try {
			socket.send(packetAck);
		} catch (IOException e) { System.out.println("Unable to send ack packet from server side."); }
		
		user.getThread().killThread();
	}
	
	private void processOutMessage(DatagramPacket packet, OutMessage outMessage){

		User user = findUserWithUid(outMessage.getUid());
		
		if (user==null) return;
		
		if (outMessage.getMessageCounter() < user.getMessageCounter()) {
			System.out.println("Packet with out message has unexpected message counter: "+outMessage.getMessageCounter()+", was expecting: "+user.getMessageCounter());
			return;
		}else if(outMessage.getMessageCounter()+1 == user.getMessageCounter()) {
			user.decrementMessageCounter();
		}
		
		for (User u : users) {
			try {
				u.getSendingMessages().put(new InMessage(user.getMessageCounter()+1,user.getUsername(),outMessage.getText()));
			} catch (InterruptedException e) { System.out.println("Unable to add in msg to queue."); }
		}
		
		AckMessage ackMessage = new AckMessage(outMessage.getMessageCounter(),user.getUid());
		
		byte[] buf = fillTheBuffer(ackMessage);
		
		DatagramPacket receivedPacket = new DatagramPacket(buf,buf.length,user.getAddress(),user.getPort());
		try {
			socket.send(receivedPacket);
			user.incrementMessageCounter();
		} catch (IOException e) { System.out.println("Unable to send ack message from server."); }
		
	}
	
	private byte[] fillTheBuffer(AckMessage ackMessage) {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		try {
			DataOutputStream dos = new DataOutputStream(bos);
			
			dos.write(2);
			dos.writeLong(ackMessage.getMessageCounter());
			dos.writeLong(ackMessage.getUid());
			
			dos.close();
			
		}catch(Exception e) {}
		
		return bos.toByteArray();
		
	}
	
	private Message getMessageFromPacket(byte[] buf, int from, int to) {
		
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(buf,from,to);
			DataInputStream dis = new DataInputStream(bis);
			
			MessageType messageType = messageTypeFromByte(dis.readByte());
			
			switch(messageType) {
			case HELLO:
				return new HelloMessage(dis.readLong(),dis.readUTF(),dis.readLong());
			case ACK:
				return new AckMessage(dis.readLong(),dis.readLong());
			case BYE:
				return new ByeMessage(dis.readLong(),dis.readLong());
			case IN:
				return new InMessage(dis.readLong(),dis.readUTF(),dis.readUTF());
			case OUT:
				return new OutMessage(dis.readLong(),dis.readLong(),dis.readUTF());
			default:
				throw new RuntimeException("Invalid state. Is impossible to happen.");
			}
			
		}catch(Exception e) { 
			throw new RuntimeException();			
		}
		
	}
	
	private MessageType messageTypeFromByte(byte b) {
		
		switch(b) {
		case 1:
			return MessageType.HELLO;
		case 2:
			return MessageType.ACK;
		case 3:
			return MessageType.BYE;
		case 4:
			return MessageType.OUT;
		case 5:
			return MessageType.IN;
		default:
			throw new RuntimeException("Invalid message type. First byte of message is expected to be from 1 to 5 inclusive.");
		}
		
	}
	
	private User findUserWithUid(long uid) {
		
		User user = null;
		
		for (User u : users) {

			if (u.getUid()==uid) {
				user = u;
				break;
			}
		}
		return user;
	}
	
}






















































































