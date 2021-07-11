package hr.fer.oprpp2.collections;

/**
 * Collection represents interface with methods that other collections will implement
 * @author leokiparje
 *
 */

public interface Collection {
	
	/**
	 * checks if collection is empty, here is implemented to always return false
	 * @return boolean value that can be true or false
	 */
	
	/**
	 * returns number of non null elements in an array. Here is implemented to always return 0
	 * @return number 0
	 */
	
	int size();
	
	/**
	 * adds the Object given in argument to the collection
	 * @param value value of an Object that will be passed to collection
	 */
	void add(Object value);
	
	/**
	 * checks if collection contains given Object. Implemented here to always return false
	 * @param value value of an Object
	 * @return boolean true of false
	 */
	boolean contains(Object value);
	
	void addAllSatisfying(Collection col, Tester tester);
	
	/**
	 * removes the element from the collection and shitfs all other elements to the according position. Implemented here to always return false
	 * @param value value of and Object
	 * @return boolean true or false
	 */
	boolean remove(Object value);
	
	/**
	 * returns array of Objects from the collection. Implemented here to always throw Exception.
	 * @return returns nothing.
	 */
	Object[] toArray();
	
	/**
	 * loop that iterates through all elements of the collection. Implemented here with empty body.
	 * @param processor reference to the object of a class Processor.
	 */
	
	/**
	 * adds all elements from collection other to the curent collection.
	 * @param other other collection
	 */
	
	/**
	 * clears the whole collection and leaves it empty with no elements. Implemented here with empty body.
	 */
	void clear();
	
	Object createElementsGetter();
	
	default void forEach(Processor processor) {
		ElementsGetter eg = (ElementsGetter) this.createElementsGetter();
		while(eg.hasNextElement()) {
			processor.process(eg.getNextElement());
		}
	}
	
	default void addAll(Collection other) {
		
		class localProcessor implements Processor {

            public void process(Object value) {
                add(value);
            }
        }

        other.forEach(new localProcessor());
		
	}
	
	default boolean isEmpty() {
		if (size()==0) return true;
		return false;
	}
}





































