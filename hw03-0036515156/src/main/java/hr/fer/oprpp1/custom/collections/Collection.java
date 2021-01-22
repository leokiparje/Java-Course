package hr.fer.oprpp1.custom.collections;

/**
 * Collection represents interface with methods that other collections will implement
 * @author leokiparje
 * @param <T> T represents type of Object stored in this collection
 */

public interface Collection<T>{
	
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
	void add(T value);
	
	/**
	 * checks if collection contains given Object. Implemented here to always return false
	 * @param value value of an Object
	 * @return boolean true of false
	 */
	boolean contains(Object value);
	
	default void addAllSatisfying(Collection<? extends T> col, Tester<? super T> tester) {
		
		ElementsGetter<? extends T> e = col.createElementsGetter();
		
		while (e.hasNextElement()) {
			T o = e.getNextElement();
			if (tester.test(o)) add(o);
		}
		
	}
	
	/**
	 * removes the element from the collection and shitfs all other elements to the according position. Implemented here to always return false
	 * @param value value of and Object
	 * @return boolean true or false
	 */
	boolean remove(T value);
	
	/**
	 * returns array of Objects from the collection. Implemented here to always throw Exception.
	 * @return returns array of Objects
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
	
	/**
	 * Method creates and returns collection's ElementsGetter
	 * @return collection's EllementsGetter
	 */
	
	ElementsGetter<T> createElementsGetter();
	
	/**
	 * Method iterates through collection using ElementGetter and processes every element
	 * @param processor represents Processor implementation
	 */
	
	default void forEach(Processor<? super T> processor) {
		ElementsGetter<T> eg = createElementsGetter();
		while(eg.hasNextElement()) {
			processor.process(eg.getNextElement());
		}
	}
	
	/**
	 * Method adds all elements from Collection other to the current collection
	 * @param other Collection whose elements will be addded to the current collecton
	 */
	
	default void addAll(Collection<? extends T> other) {
		
		class localProcessor implements Processor<T> {

            public void process(T value) {
                add(value);
            }
        }

        other.forEach(new localProcessor());
		
	}
	
	/**
	 * Method checks if Collection is empty.
	 * @return boolean true or false
	 */
	
	default boolean isEmpty() {
		if (size()==0) return true;
		return false;
	}
}





































