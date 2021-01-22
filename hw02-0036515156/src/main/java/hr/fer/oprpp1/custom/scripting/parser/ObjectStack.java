package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;

import hr.fer.oprpp1.custom.collections.EmptyStackException;
import hr.fer.oprpp1.custom.scripting.nodes.*;

/**
 * Class that represents stack.
 * @author leokiparje
 *
 */
public class ObjectStack {
	
	/**
	 * reference stack to the newly created Object of the class
	 */
	private ArrayIndexedCollection stack = new ArrayIndexedCollection();
	
	/**
	 * checks if stack is empty
	 * @return boolean true or false
	 */
	public boolean isEmpty() {
		return stack.isEmpty();
	}
	
	/**
	 * returns number of elements from the stack
	 * @return number of elements from the stack
	 */
	public int size() {
		return stack.size();
	}
	
	/**
	 * method that adds Object to the last position in stack
	 * @param value value of added Object
	 */
	public void push(Object value) {
		stack.add(value);
	}
	
	/**
	 * removes the last added object from the stack
	 * @return Object that was removed
	 */
	public Object pop() {
		if (size()==0) throw new EmptyStackException();
		Object result = peek();
		stack.remove(size()-1);
		return result;
	}
	
	/**
	 * peeks and returns last added object on the stack
	 * @return last added object of the stack
	 */
	public Object peek() {
		if (size()==0) throw new EmptyStackException();
		return stack.get(size()-1);
	}

	/**
	 * removes all elements from the stack and leaves it empty
	 */
	public void clear() {
		stack.clear();
	}
	
}
