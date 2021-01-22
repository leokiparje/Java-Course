package hr.fer.oprpp1.hw05.shell;

/**
 * Class represents excpetion that extends RuntimeException
 * @author leokiparje
 *
 */

public class ShellIOException extends RuntimeException{
	
	public ShellIOException() {
		super();
	}
	public ShellIOException(String message) {
		super(message);
	}
}
