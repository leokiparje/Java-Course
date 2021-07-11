package hr.fer.oprpp2.collections;

/**
 * Interface ElementsGetter
 * @author leokiparje
 *
 */

public interface ElementsGetter {
	
	/**
	 * returns boolean value if element has next element
	 * @return
	 */
	
	boolean hasNextElement();
	
	/**
	 * getter for next Element
	 * @return boolean true or false
	 */
	
	Object getNextElement();
	
	/**
	 * process remaining elements
	 * @param p Processor
	 */
	
	void processRemaining(Processor p);
	
}
