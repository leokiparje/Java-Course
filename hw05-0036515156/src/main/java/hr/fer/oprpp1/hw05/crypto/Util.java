package hr.fer.oprpp1.hw05.crypto;

/**
 * Class Util has two static methods used for conversion hex String to byte array and reverse
 * @author leokiparje
 *
 */

public class Util {
	
	/**
	 * Method hextobyte converts hey String to byte array
	 * @param keyText hex String
	 * @return byte array
	 */

	public static byte[] hextobyte(String keyText) {
		
		if (keyText.length()%2!=0) throw new IllegalArgumentException("Odd-sized string!");
		
		byte[] bytes = new byte[keyText.length()/2];
		
		for (int i=0;i<keyText.length();i+=2) {
			byte prvi = asciiToByte(keyText.charAt(i));
			byte drugi = asciiToByte(keyText.charAt(i+1));
			
			if (prvi>7) {
				
				bytes[i/2]=0;
				int[] binaryarray = new int[8];
				for (int k=0;k<4;k++) {
					binaryarray[k] = (byte) Math.abs(drugi%2-1);
					drugi/=2;
				}				
				for (int l=4;l<8;l++) {
					binaryarray[l] = (byte) Math.abs(prvi%2-1);
					prvi/=2;
				}
				for (int k=0;k<8;k++) {
					if (binaryarray[k]==1) {
						binaryarray[k]=0;
					}else {
						binaryarray[k]=1;
						break;
					}
				}
				int p=1;
				for (int b : binaryarray) {
					bytes[i/2] += b*p;
					p*=2;
				}
				bytes[i/2]*=-1;
				
			}else {				
				bytes[i/2] = (byte) (drugi + 16*prvi);
			}
			
		}
		
		return bytes;
		
	}
	
	/**
	 * Method bytetohex converts byte array to hex String
	 * @param bytearray byte array
	 * @return hex String
	 */
	
	public static String bytetohex(byte[] bytearray) {
		
		StringBuilder sb = new StringBuilder();
		
		for (byte b : bytearray) {
			
			if (b>0) {
				if (b>15) {
					int n = b%16;
					int k = b/16;
					sb.append(k);
					if (n>9) sb.append(numToChar(n));					
					else sb.append(n);
				}else {
					sb.append(0);
					if (b>9) sb.append(numToChar(b));
					else sb.append(b);
				}
			}else if (b==-128) {
				sb.append("80");
			}else if(b==0) {
				sb.append("00");
			}else if(b<0){
				
				b*=-1;
				int[] bytes = new int[8];
				int index=0;
				while(b>0) {
					bytes[index++] = Math.abs(b%2-1);
					b/=2;
				}
				for (int i = index;i<8;i++) {
					bytes[i] = 1;
				}
				for (int k=0;k<8;k++) {
					if (bytes[k]==1) {
						bytes[k]=0;
					}else {
						bytes[k]=1;
						break;
					}
				}
				int h=0;
				int p=1;
				for (int i=4;i<8;i++) {
					h+=(bytes[i]*p);
					p*=2;
				}
				if (h>9) sb.append(numToChar(h));
				else sb.append(h);
				
				h=0;
				p=1;
				for (int i=0;i<4;i++) {
					h+=(bytes[i]*p);
					p*=2;
				}
				if (h>9) sb.append(numToChar(h));
				else sb.append(h);	
			}			
		}		
		return sb.toString();	
	}
	
	/**
	 * Helper method that takes a number in ascii value and returns the value of its integer representation
	 * @param num integer number
	 * @return byte
	 */
	
	public static byte asciiToByte(int num) {
		switch(num) {
			case 48:
				return 0;
			case 49:
				return 1;
			case 50:
				return 2;
			case 51:
				return 3;
			case 52:
				return 4;
			case 53:
				return 5;
			case 54:
				return 6;
			case 55:
				return 7;
			case 56:
				return 8;
			case 57:
				return 9;
			case 65:
				return 10;
			case 97:
				return 10;
			case 66:
				return 11;
			case 98:
				return 11;
			case 67:
				return 12;
			case 99:
				return 12;
			case 68:
				return 13;
			case 100:
				return 13;
			case 69:
				return 14;
			case 101:
				return 14;
			case 70:
				return 15;
			case 102:
				return 15;
			default:
				throw new IllegalArgumentException("Number is not in range 10 to 15");
		}
	}
	
	/**
	 * Method numToChar takes a number and retunrs character representation of it
	 * @param num integer number
	 * @return String representation of a number
	 */
	
	public static String numToChar(int num) {
		switch(num) {
			case 10:
				return "a";
			case 11:
				return "b";
			case 12:
				return "c";
			case 13:
				return "d";
			case 14:
				return "e";
			case 15:
				return "f";
			default:
				throw new IllegalArgumentException("Number is not in range 10 to 15");
		}
	}
	
}




















































































