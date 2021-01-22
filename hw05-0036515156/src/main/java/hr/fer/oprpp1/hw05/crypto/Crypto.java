package hr.fer.oprpp1.hw05.crypto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class Crypto has two methods, checksha and crypto. Checksha is used to check if given hash matches expected one.
 * Method Crpyto is used to encrypt and decrypt data.
 * @author leokiparje
 *
 */

public class Crypto {
	
/**
 * This method is used for checking if checksum of given file equals expected checksum.
 * @param file String path to the file that will be checked
 * @param hash Sting representation of expected checksum
 */
	
	public static void checksha(String file, String hash) {
		
		try(InputStream is = Files.newInputStream(Paths.get(file))){
			
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] buff = new byte[4096];
			
			while(true) {
				
				int r = is.read(buff);		
				if (r<1) break;
				
				digest.update(buff, 0, r);
				
			}
			
			byte[] result = digest.digest();
			if (hash.equals(Util.bytetohex(result))) {
				System.out.println("Digesting completed. Digest of "+file+" matches expected digest.");
			}else {
				System.out.println("Digesting completed. Digest of "+file+" does not match the expected digest. Digest\n" + 
						"was: 2e7b3a91235ad72cb7e7f6a721f077faacfeafdea8f3785627a5245bea112598");
			}
			
		}catch(Exception e) {
			System.out.println("Exception while digesting!");
		}
	}
	
	/**
	 * Method is used to encrypt and decrypt files
	 * @param input String of source file
	 * @param output String of destination file
	 * @param encrypt boolean value true or false
	 * @param key String representation of key with whom we will encrypt and decrypt data
	 * @param initialVector String representation of initial vector used for crypting
	 */
	
	public static void crypt(String input, String output, boolean encrypt, String key, String initialVector) {
		
		InputStream is = null;
		OutputStream os = null;
		
		try {
			is = Files.newInputStream(Paths.get(input));
			os = Files.newOutputStream(Paths.get(output),StandardOpenOption.CREATE);
		}catch(Exception e) {
			System.out.println(e);
		}
		
		SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(key), "AES");		
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(initialVector));
		
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");		
			cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
		}catch(Exception e) {
			System.out.println("Unable to create cipher object!");
		}
		
		byte[] buff = new byte[4096];
		
		while(true) {
			try {
				int r = is.read(buff);
				if (r<1) break;	
				os.write(cipher.update(buff, 0, r));
			}catch(Exception e) {
				System.out.println("Unable to read the buffer!");
			}
			
		}
		try {
			os.write(cipher.doFinal());
		}catch(Exception e) {
			System.out.println("Bad padding exception on doFinal() method call!");
		}
		
		if (encrypt) {
			System.out.println("Encryption completed. Generated file "+output+" based on file "+input+".");
		}else {
			System.out.println("Decryption completed. Generated file "+output+" based on file "+input+".");
		}
	
		
	}
	
	/**
	 * Method main
	 * @param args Command line args
	 */
		
	public static void main(String[] args) {
		
		if (args.length<2 || args.length>3) throw new IllegalArgumentException();
		if (args[0].equals("checksha")) {
			if (args.length!=2) throw new IllegalArgumentException();
			try(Scanner sc = new Scanner(System.in)){
				
				String line;
		        System.out.print("Please provide expected sha-256 digest for hw05test.bin:\n"+"> ");	              	
		        line = sc.nextLine();
		        checksha(args[1],line);
		        
			}catch(Exception e) {
				System.out.println("Exception while reading input!");
			}
	        
		}else if (args[0].equals("encrypt")) {
			if (args.length!=3) throw new IllegalArgumentException();
			
			try(Scanner sc = new Scanner(System.in)){
				
				String key;
				String initalVector;
				System.out.print("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n> ");	              	
		        key = sc.nextLine();
		        System.out.print("Please provide initialization vector as hex-encoded text (32 hex-digits):\n> ");
		        initalVector = sc.nextLine();
		        crypt(args[1], args[2], true, key, initalVector);
		        
			}catch(Exception e) {
				System.out.println("Exception while reading input!");	
			}
			
		}else if (args[0].equals("decrypt")) {
			if (args.length!=3) throw new IllegalArgumentException();
			
			try(Scanner sc = new Scanner(System.in)){
				
				String key;
				String initalVector;
				System.out.print("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n> ");	              	
		        key = sc.nextLine();
		        System.out.print("Please provide initialization vector as hex-encoded text (32 hex-digits):\n> ");
		        initalVector = sc.nextLine();
		        crypt(args[1], args[2], false, key, initalVector);
		        
			}catch(Exception e) {
				System.out.println("Exception while reading input!");	
			}
			
		}else {
			System.out.println("Wrong arguments given!");
		}	
	}	
}


























































































