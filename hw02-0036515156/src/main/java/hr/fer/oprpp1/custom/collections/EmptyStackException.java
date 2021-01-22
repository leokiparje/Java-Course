package hr.fer.oprpp1.custom.collections;

/**
 * Class of Exception that is a RuntimeException type 
 * @author leokiparje
 *
 */
public class EmptyStackException extends RuntimeException{
	public EmptyStackException() {}
	public EmptyStackException(String message) {
		super(message);
	}
	public EmptyStackException(String message, Throwable cause) {
		super(message,cause);
	}
	public EmptyStackException(Throwable cause) {
		super(cause);
	}
	public EmptyStackException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
