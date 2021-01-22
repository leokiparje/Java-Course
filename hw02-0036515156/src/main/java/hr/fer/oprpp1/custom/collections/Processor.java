package hr.fer.oprpp1.custom.collections;

/**
 * This interface represents processor. It has only one method process which is to be implemented.
 * @author leokiparje
 *
 */
public interface Processor {
	
	/**
	 * method processes the given object
	 * @param value of the Object we want to process
	 */
	void process(Object value);
	
}
