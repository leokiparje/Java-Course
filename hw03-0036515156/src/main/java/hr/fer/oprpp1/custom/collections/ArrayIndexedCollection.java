package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Class represents array list with all functional methods.
 * @author leokiparje
 * @param <T> T represents type of Object stored in this collection
 */
public class ArrayIndexedCollection<T> implements List<T>{
	
	/**
	 * Current size of collection
	 */
	private int size;
	
	/**
	 * Array of type T that is internally stored in class
	 */
	private T[] elements;
	
	/**
	 * Integer value of maximum elements in an array
	 */
	private int capacity;
	
	private long modificationCount = 0;
	
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
	@SuppressWarnings("unchecked")
	public ArrayIndexedCollection(int initialCapacity) {
		if (initialCapacity<1) throw new IllegalArgumentException();
		capacity=initialCapacity;
		elements = (T[]) new Object[capacity];
		size=0;
	}
	
	/**
	 * Constructor with one Collection argument
	 * @param other refers to the Collection that is passed as an argument
	 */
	public ArrayIndexedCollection(Collection<T> other) {
		this(other, 16);
	}
	
	/**
	 * 
	 * @param other refers to the Collection that is passed as an argument
	 * @param initialCapacity initial capacity
	 */
	public ArrayIndexedCollection(Collection<T> other, int initialCapacity) {
		if (other==null) throw new NullPointerException();
		if (initialCapacity<1) throw new IllegalArgumentException();
		if (capacity<other.size()) capacity=other.size();
		else capacity=initialCapacity;
		elements = (T[]) new Object[capacity];
		addAll(other);
	}
	
	/**
	 * Class ArrayGetter represents one ElementGetter which will be used to iterate through collection.
	 * @author leokiparje
	 *
	 * @param <T> T represents type of Object stored in this collection
	 */
	
	private static class ArrayGetter<T> implements ElementsGetter<T> {
		
		/**
		 * kopija is a refrence to the outerclass instance 
		 */
		
		private ArrayIndexedCollection<T> kopija;
		
		/**
		 * long that stores a number of modifications on the collection
		 */
		
		private long savedModificationCount;
		
		/**
		 * Current index of ElementsGetter
		 */
		
		private int index=0;
		
		/**
		 * Array of type T 
		 */
		
		private T[] polje;
		
		/**
		 * Basic constructor
		 * @param areja represents instance of class ArrayIndexedCollection
		 */
		
		public ArrayGetter(ArrayIndexedCollection<T> areja) {
			kopija = areja;
			polje = areja.elements;
			savedModificationCount = areja.modificationCount;
		}
		
		/**
		 * Method checks if collection has next element.
		 */
		
		public boolean hasNextElement() {
			if (savedModificationCount!=kopija.modificationCount) throw new ConcurrentModificationException();
			if (kopija.getElements().length<=index || polje[index]==null) return false;
			return true;
		}
		
		/**
		 * Method returns next element or throws exception if there is none.
		 */
		
		public T getNextElement() {
			if (savedModificationCount!=kopija.modificationCount) throw new ConcurrentModificationException();
			if (hasNextElement()) return polje[index++];
			throw new NoSuchElementException();
		}
		
		/**
		 * Method processes the remaining elements of the collection with instance of Processor 
		 */
		
		public void processRemaining(Processor<T> p) {		
			if (savedModificationCount!=kopija.modificationCount) throw new ConcurrentModificationException();
			
			while (polje[index]!=null) {
				p.process(polje[index++]);
			}
			
		}
			
	}
	
	/**
	 * Method that creates ElementsGetter on specific instance of collection
	 */
	
	public ElementsGetter<T> createElementsGetter() {
		
		return new ArrayGetter<T>(this);
		
	}
	
	/**
	 * Adds all elements from given collection col that passes tester's test to current collection
	 */
	
	public void addAllSatisfying(Collection col, Tester tester) {
		
		ElementsGetter<T> eg = (ElementsGetter) col.createElementsGetter();
		
		while (eg.hasNextElement()) {
			T o = eg.getNextElement();
			if (tester.test(o)) add(o);
		}
		
	}
	
	/**
	 * returns array of type T
	 * @return array of type T
	 */
	
	
	public T[] getElements() {
		if (size()==0) throw new NullPointerException();
		return elements;
	}
	
	/**
	 * Method checks if collection is empty.
	 */
	
	public boolean isEmpty() {
		if (size()==0) return true;
		return false;
	}
	/*
	public void addAll(Collection<? extends T> other) {
		
		class localProcessor implements Processor<T> {

            public void process(Object value) {
                add((T)value);
            }
        }

        other.forEach(new localProcessor());
		
	}
	*/
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
		modificationCount++;
	}
	
	/**
	 * Method removes given element from collection and returns true if it exists inside of it, otherwise it returns false
	 */
	
	public boolean remove(T value) {
		if (value==null) throw new NullPointerException();
		
		if (contains(value)) {
			for (int i = indexOf(value);i<size-1;i++) {
				elements[i]=elements[i+1];
			}
			elements[size]=null;
			size--;
			modificationCount++;
			return true;
		}else {
			return false;
		}	
	}
	
	/**
	 * Adds given Object to the collection.
	 */
	
	public void add(T value) {
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
	public T get(int index) {
		if (index<0 || index>(size-1)) throw new IndexOutOfBoundsException();
		return elements[index];
	}
	
	/**
	 * Removes all objects from the collection.
	 */
	
	public void clear() {
		for (int i=0;i<size;i++) {
			elements[i]=null;
		}
		size=0;
		modificationCount++;
	}
	
	/**
	 * Average complexity of this method is |size|
	 * @param value value of Object we want to insert in our array
	 * @param position index of position
	 */
	public void insert(T value, int position) {
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
		modificationCount++;
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
	
	/**
	 * Method returns size.
	 */
	
	public int size() {
		return size;
	}
	
	/**
	 * Method converts current collection to array of Objects.
	 */
	
	public Object[] toArray() {
		
		elements = Arrays.copyOf(elements, size);
		return elements;
		
	}
	
}








































































