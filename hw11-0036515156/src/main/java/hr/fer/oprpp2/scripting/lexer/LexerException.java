package hr.fer.oprpp2.scripting.lexer;

/**
 * class LexerException extends RuntimeException
 * @author leokiparje
 *
 */

public class LexerException extends RuntimeException{

	/**
	 * default constructor
	 */
	
	public LexerException() {}
	
	/**
	 * constructor with parametar String message
	 * @param message String message
	 */
	
	public LexerException(String message) {
		super(message);
	}
	
}
