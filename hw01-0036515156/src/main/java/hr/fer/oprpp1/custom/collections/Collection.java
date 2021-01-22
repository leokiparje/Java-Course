package hr.fer.oprpp1.custom.collections;

/**
 * Main class which will represent all kinds of collections. It had mainly empty method bodies which are going to be implemented later
 * @author leokiparje
 *
 */

public class Collection {

	/**
	 * default constructor
	 */
	protected Collection() {}
	
	/**
	 * checks if collection is empty, here is implemented to always return false
	 * @return boolean value that can be true or false
	 */
	public boolean isEmpty() {
		if (size()==0) return true;
		return false;
	}
	
	/**
	 * returns number of non null elements in an array. Here is implemented to always return 0
	 * @return number 0
	 */
	
	public int size() {
		return 0;
	}
	
	/**
	 * adds the Object given in argument to the collection
	 * @param value value of an Object that will be passed to collection
	 */
	public void add(Object value) {}
	
	/**
	 * checks if collection contains given Object. Implemented here to always return false
	 * @param value value of an Object
	 * @return boolean true of false
	 */
	public boolean contains(Object value) {
		return false;
	}
	
	/**
	 * removes the element from the collection and shitfs all other elements to the according position. Implemented here to always return false
	 * @param value value of and Object
	 * @return boolean true or false
	 */
	public boolean remove(Object value) {
		return false;
	}
	
	/**
	 * returns array of Objects from the collection. Implemented here to always throw Exception.
	 * @return returns nothing.
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * loop that iterates through all elements of the collection. Implemented here with empty body.
	 * @param processor reference to the object of a class Processor.
	 */
	public void forEach(Processor processor) {}
	
	/**
	 * adds all elements from collection other to the curent collection.
	 * @param other other collection
	 */
	public void addAll(Collection other) {
		class LocalProcessor extends Processor{
			
			public void process(Object value) {
				add(value);
			}
		}	
		other.forEach(new LocalProcessor());
		
	}
	
	/**
	 * clears the whole collection and leaves it empty with no elements. Implemented here with empty body.
	 */
	public void clear() {}
}





































