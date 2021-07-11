package hr.fer.oprpp2.collections;

/**
 * class represents Exception and extends RuntimeException
 * @author leokiparje
 *
 */

public class ConcurrentModificationException extends RuntimeException{
	
	/**
	 * default constructor
	 */
	
	public ConcurrentModificationException() {}
	
	/**
	 * basic contrusctor
	 * @param message String message
	 */
	
	public ConcurrentModificationException(String message) {
		System.out.println(message);
	}
}
