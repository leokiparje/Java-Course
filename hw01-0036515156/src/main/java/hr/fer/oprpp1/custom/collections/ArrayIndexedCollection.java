package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;

/**
 * Class represents array list with all functional methods.
 * @author leokiparje
 *
 */
public class ArrayIndexedCollection extends Collection{
	
	/**
	 * Current size of collection
	 */
	private int size;
	
	/**
	 * Array of Objects that is internally stored in class
	 */
	private Object[] elements;
	
	/**
	 * Integer value of maximum elements in an array
	 */
	private int capacity;
	
	/**
	 * Default constructor
	 */
	public ArrayIndexedCollection() {
		this(16);
	}
	
	/**
	 * Constructor with one integer argument
	 * @param initialCapacity Initial capacity
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if (initialCapacity<1) throw new IllegalArgumentException();
		capacity=initialCapacity;
		elements = new Object[capacity];
		size=0;
	}
	
	/**
	 * Constructor with one Collection argument
	 * @param other refers to the Collection that is passed as an argument
	 */
	public ArrayIndexedCollection(Collection other) {
		this(other, 16);
	}
	
	/**
	 * 
	 * @param other refers to the Collection that is passed as an argument
	 * @param initialCapacity initial capacity
	 */
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		if (other==null) throw new NullPointerException();
		if (initialCapacity<1) throw new IllegalArgumentException();
		if (capacity<other.size()) capacity=other.size();
		else capacity=initialCapacity;
		elements = new Object[capacity];
		addAll(other);
	}
	
	public boolean contains(Object value) {
		for (int i=0;i<size;i++) {
			if (elements[i].equals(value)) return true;
		}
		return false;
	}
	
	/**
	 * removes the Object of an array at the given index. Throws IndexOutOfBoundsException if index is not valid
	 * @param index position of element we want to remove from the array
	 */
	public void remove(int index) {
		if (index<0 || index>size-1) throw new IndexOutOfBoundsException();
		
		for (int i=index;i<size-1;i++) {
			elements[i]=elements[i+1];
		}
		elements[--size]=null;
	}
	
	public boolean remove(Object value) {
		if (value==null) throw new NullPointerException();
		
		if (contains(value)) {
			for (int i = indexOf(value);i<size-1;i++) {
				elements[i]=elements[i+1];
			}
			elements[--size]=null;
			return true;
		}else {
			return false;
		}	
	}
	
	
	public void add(Object value) {
		if (value==null) throw new NullPointerException();
		if (size==capacity) {
			capacity+=capacity;
			elements = Arrays.copyOf(elements, capacity);
		}
		
		elements[size++] = value;
	}
	
	/**
	 * Average complexity of this method is 1;
	 * @param index position of Object in array that we want to print
	 * @return Object at the given index
	 */
	public Object get(int index) {
		if (index<0 || index>(size-1)) throw new IndexOutOfBoundsException();
		return elements[index];
	}
	
	public void forEach(Processor processor) {
		for (int i=0;i<size;i++) {
			processor.process(elements[i]);
		}
	}
	
	public void clear() {
		for (int i=0;i<size;i++) {
			elements[i]=null;
		}
		size=0;
	}
	
	/**
	 * Average complexity of this method is |size|
	 * @param value value of Object we want to insert in our array
	 * @param position index of position
	 */
	public void insert(Object value, int position) {
		if (value==null) throw new NullPointerException();
		if (position<0 || position>size) throw new IndexOutOfBoundsException();
		if (size==capacity) {
			capacity+=capacity;
			elements = Arrays.copyOf(elements, capacity);
		}	
		for (int i=size;i>position;i--) {
			elements[i] = elements[i-1];
		}
		elements[position]=value;
		size++;
	}
	
	/**
	 * Average complexity of this method is |size|
	 * @param value value of Object we want to find index of
	 * @return index of Object in array, or in case of not finding the Object it returns -1.
	 */
	public int indexOf(Object value) {
		for(int i=0;i<size;i++) {
			if (elements[i].equals(value)) return i;
		}
		return -1;
	}
	
	public int size() {
		return size;
	}
	
	public Object[] toArray() {
		return elements;
	}
	
	public void addAll(Collection other) {
		class LocalProcessor extends Processor{
			
			public void process(Object value) {
				add(value);
			}
		}	
		other.forEach(new LocalProcessor());
		
	}
	
}








































































