package hr.fer.oprpp1.custom.collections;

/**
 * Interface ElementsGetter
 * @author leokiparje
 * @param <T> T represents type of Object this getter will iterate on
 */

public interface ElementsGetter<T> {
	
	/**
	 * returns boolean value if element has next element
	 * @return boolean true or false
	 */
	
	boolean hasNextElement();
	
	/**
	 * getter for next Element
	 * @return boolean true or false
	 */
	
	T getNextElement();
	
	/**
	 * process remaining elements
	 * @param p Processor
	 */
	
	void processRemaining(Processor<T> p);
	
}
