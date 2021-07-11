package hr.fer.oprpp2.chatApp;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Class Client represents a client in UDP protocol chat application. When client is run, GUI window opens and client can send messages to
 * any other client connected to the same socket, ip address and port. When client sends a message every other client on the network receives it
 * and message is displayed on their chat window. Client who sent the message also sees his own message.
 * @author leokiparje
 *
 */

public class Client {

	private InetAddress destAddress;
	private int destPort;
	
	private DatagramSocket socket;
	
	private long clientCounter;
	private long serverCounter;
	
	private long uid;
	private String username;
	
	private BlockingQueue<Message> receivedMessages;
	
	private JTextArea textArea;
	private JTextField textField;
	
	private boolean isOpen;
	
	private ClientListener clientListening;
	
	public Client(InetAddress destAddress, int destPort, DatagramSocket socket, long uid, String username) {
		this.destAddress = destAddress;
		this.destPort = destPort;
		this.socket = socket;
		this.uid = uid;
		this.username = username;
		
		clientCounter = 0;
		serverCounter = 1;
		
		isOpen = true;
		
		receivedMessages = new LinkedBlockingQueue<>();
		
		initGUI();
		
		clientListening = new ClientListener();
		clientListening.start();
	}
	
	public class ClientListener extends Thread {
		
		public void run() {
			
			while(true){
				
				byte[] buf = new byte[4000];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				
				try {
					socket.receive(packet);
				}catch(SocketTimeoutException se) {
					if (!isOpen) return;
					continue;
				}catch(Exception e) {
					if (!isOpen) return;
					System.out.println("Error occured, will try again.");
					continue;
				}
				
				Message receivedMessage = null;
				
				try {
					receivedMessage = getMessageFromPacket(packet.getData(),packet.getOffset(),packet.getLength());
				}catch(Exception e) {
					if (!isOpen) return;
					System.out.println("Error while creating message from received packet on client side.");
					continue;
				}
				switch(receivedMessage.getMessageType()) {
				case ACK:
					try {
						receivedMessages.put(receivedMessage);
					} catch (InterruptedException e) { System.out.println("Error occured while trying to put message to queue."); }
					continue;
					
				case IN:
					InMessage inMessage = (InMessage)receivedMessage;
					/*
					if (inMessage.getMessageCounter() != serverCounter) {
						System.out.println("Primio sam in poruku s brojem: "+inMessage.getMessageCounter()+", a očekivao sam broj: "+serverCounter);
						continue;
					}else if(inMessage.getMessageCounter()+1 == serverCounter) {
						serverCounter--;
					}
					 */
					textArea.append(packet.getSocketAddress() + " Poruka od korisnika: " + inMessage.getUsername() + "\n" + inMessage.getText() + "\n");
					
					AckMessage ackMessage = new AckMessage(inMessage.getMessageCounter(),uid);
					sendAck(ackMessage);
					
					serverCounter++;
					
					continue;
					
				default:
					System.out.println("Dobio sam krivi tip poruke. Očekivao sam ack ili in.");
				}
				
			}
			
		}
		
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
	
	private void sendAck(AckMessage ackMessage) {
		
		byte[] buf = fillTheBuffer(ackMessage);
		
		DatagramPacket packet = new DatagramPacket(buf, buf.length, destAddress, destPort);
		
		try {
			socket.send(packet);
		}catch(Exception e) {
			System.out.println("Unable to send ack packet back to server.");
		}
		
	}
	
	private byte[] fillTheBuffer(AckMessage ackMessage) {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		try {
			DataOutputStream dos = new DataOutputStream(bos);
			
			dos.write(2);
			dos.writeLong(ackMessage.getMessageCounter());
			dos.writeLong(uid);
			
			dos.close();
			
		}catch(Exception e) {}
		
		return bos.toByteArray();
		
	}
	
	private byte[] fillTheBuffer(ByeMessage byeMessage) {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		try {
			DataOutputStream dos = new DataOutputStream(bos);
			
			dos.write(3);
			dos.writeLong(byeMessage.getMessageCounter());
			dos.writeLong(uid);
			
			dos.close();
			
		}catch(Exception e) {}
		
		return bos.toByteArray();
		
	}
	
	private static byte[] fillTheBuffer(HelloMessage helloMessage) {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		try {
			DataOutputStream dos = new DataOutputStream(bos);
			
			dos.write(1);
			dos.writeLong(0);
			dos.writeUTF(helloMessage.getUsername());
			dos.writeLong(helloMessage.getRandKey());
			
			dos.close();
			
		}catch(Exception e) {}
		
		return bos.toByteArray();
		
	}
	
	private void initGUI() {
		
		JFrame frame = new JFrame("Chat client: "+username);
		
		frame.getContentPane().setLayout(new BorderLayout());
		
		textField = new JTextField();
		frame.getContentPane().add(textField, BorderLayout.NORTH);
		
		textArea = new JTextArea();
		frame.getContentPane().add(new JScrollPane(textArea),BorderLayout.CENTER);
		
		setListenerForTextField(textField);
		setClosingAction(frame);
		
		setFrame(frame);
		
	}
	
	private void setListenerForTextField(JTextField textField) {
		
		textField.addActionListener(
					(e) -> {
						String text = textField.getText();
						
						int failCounter = 0;
						
						while (failCounter < 10) {
							
							failCounter++;
							
							sendOutMessage(text);
							
							Message message = null;
							
							try {
								message = receivedMessages.poll(5,TimeUnit.SECONDS);
							} catch (InterruptedException e1) { 
								System.out.println("Unable to poll ack message on the client side.");
								continue;
							}
							
							if (message.getMessageType()!=MessageType.ACK) {
								System.out.println("Received message that is not ack. I'll try again.");
								continue;
							}
							
							AckMessage ackMessage = (AckMessage) message;
							
							if (ackMessage.getMessageCounter()!=clientCounter || ackMessage.getUid()!=uid) {
								System.out.println("Ack message received with wrong message counter or uid. I'll try again.");
								continue;
							}else break;
						}
						serverCounter++;
						textField.setText("");
					}
				);
		
	}
	
	private void setClosingAction(JFrame frame) {
		
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				
				int failCounter = 0;
				
				while(failCounter < 10) {
					
					failCounter++;
					
					ByeMessage byeMessage = new ByeMessage(++clientCounter,uid);
					
					byte[] buf = fillTheBuffer(byeMessage);
					
					DatagramPacket packet = new DatagramPacket(buf, buf.length, destAddress, destPort);
					
					try {
						socket.send(packet);
					}catch(Exception ex) {
						System.out.println("Unable to send bye packet to server.");
					}
					
					Message message = null;
					
					try {
						message = receivedMessages.poll(5, TimeUnit.SECONDS);
					} catch (InterruptedException e1) {}
					
					if (message.getMessageType()!=MessageType.ACK) {
						System.out.println("Received message that is not ack. I'll try again.");
						continue;
					}
					
					AckMessage ackMessage = (AckMessage) message;
					
					if (ackMessage.getMessageCounter()!=clientCounter || ackMessage.getUid()!=uid) {
						System.out.println("Ack message received with wrong message counter or uid. I'll try again.");
						continue;
					}else break;
					
				}
				
				isOpen = false;
				socket.close();
				frame.dispose();
				
			}
		});
		
	}
	
	private void sendOutMessage(String text) {
		
		OutMessage outMessage = new OutMessage(++clientCounter,uid,text);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		try {
			DataOutputStream dos = new DataOutputStream(bos);
			
			dos.write(4);
			dos.writeLong(outMessage.getMessageCounter());
			dos.writeLong(uid);
			dos.writeUTF(text);
			
			dos.close();
			
		}catch(Exception e) {}
		
		byte[] buf = bos.toByteArray();
		
		DatagramPacket packet = new DatagramPacket(buf,buf.length,destAddress,destPort);
		
		try {
			socket.send(packet);
		}catch(Exception e) {
			System.out.println("Unable to send out message from client to server.");
		}
		
	}
	
	private void setFrame(JFrame frame) {
		frame.setSize(400,400);
		frame.setVisible(true);
	}
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		
		if (args.length!=3) {
			System.out.println("Dragi korisniče, očekivao sam 3 parametra: ip adresu, port i username");
			return;
		}
		
		final InetAddress destAddress = InetAddress.getByName(args[0]);
		
		int destPort = Integer.parseInt(args[1]);
		if (destPort<1 || destPort>65535) {
			System.out.println("Port brojevi idu od 1 do 65535.");
			return;
		}
		
		String username = args[2];
		Random rand = new Random();
		long randKey = rand.nextLong() & Long.MAX_VALUE; // To ensure my randKey will be a positive number.
		final DatagramSocket socket = new DatagramSocket();
		
		HelloMessage helloMessage = new HelloMessage(0,username,randKey);
		
		int failCounter = 0;

		while(failCounter<10) {
			failCounter++;
			
			byte[] buf = fillTheBuffer(helloMessage);
			
			DatagramPacket packet = new DatagramPacket(buf,buf.length,destAddress,destPort);
			try {
				socket.send(packet);
			} catch (IOException e) { System.out.println("Unable to send hello message from opened socket."); }
			
			byte[] receivedBuf = new byte[4000];

			DatagramPacket receivedPacket = new DatagramPacket(receivedBuf,receivedBuf.length);

			try {
				socket.setSoTimeout(5000);
			} catch (SocketException e) { System.out.println("Unexpected error, couldn't set timeout for socket"); continue; }
			
			try {
				socket.receive(receivedPacket);
			}catch(SocketTimeoutException e) {
				continue;
			}catch(Exception ex) {
				System.out.println("Unexpected error with opened socket.");
				return;
			}
			try {
				ByteArrayInputStream bis = new ByteArrayInputStream(receivedPacket.getData(),receivedPacket.getOffset(),receivedPacket.getLength());
				DataInputStream dis = new DataInputStream(bis);
				
				byte messageType = dis.readByte();
				long messageCounter = dis.readLong();
				final long uidFromAck = dis.readLong();
				
				if (messageType!=2 || messageCounter!=0) {
					throw new RuntimeException();
				}else {
					SwingUtilities.invokeLater(() -> {
						Client client = new Client(destAddress,destPort,socket,uidFromAck,username);
					});
					break;
				}
				
			}catch(Exception e) {
				System.out.println("Error occured while reading ack message from the packet");
				continue;
			}
		}
		
	}
	
}





































































































