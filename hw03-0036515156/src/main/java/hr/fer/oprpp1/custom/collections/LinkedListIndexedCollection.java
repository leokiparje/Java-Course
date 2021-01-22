package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Collection sotres elements in a double linked list with reference to the first and last elements.
 * @author leokiparje
 *
 * @param <T> T represents type of Object stored in this collection
 */
public class LinkedListIndexedCollection<T> implements List<T> {
	
	/**
	 * private class that represents a node 
	 * @author leokiparje
	 *
	 */
	private static class ListNode<T> {
		
		/**
		 * ListNode of the previous node
		 */
		ListNode<T> prethodni;
		
		/**
		 * ListNode of the next node
		 */
		ListNode<T> sljedeci;
		
		/**
		 * Value of the object
		 */
		T value;
	}
	
	/**
	 * size of the linked list
	 */
	private int size;
	
	/**
	 * reference to the first node of the list
	 */
	private ListNode<T> first;
	
	/**
	 * reference of the last node of the list
	 */
	private ListNode<T> last;
	
	private long modificationCount = 0;
	
	/**
	 * default constructor
	 */
	public LinkedListIndexedCollection() {
		
		size=0;
		first=null;
		last=null;
		
	}
	
	/**
	 * Basic constructor
	 * @param other Collection whose elements will be added to the newly created Collection
	 */
	
	public LinkedListIndexedCollection(Collection<T> other) {
		if (other==null) throw new NullPointerException();
		addAll(other);
		
	}
	
	/**
	 * class ListGetter represents one ElementsGetter which will be used to iterate through the collection.
	 * @author leokiparje
	 *
	 * @param <T> T represents type of Object stored in this collection
	 */
	
	private static class ListGetter<T> implements ElementsGetter<T>{
		
		/**
		 * long that stores a number of modifications on the collection
		 */
		
		private long savedModificationCount;
		
		/**
		 * kopija is a refrence to the outerclass instance 
		 */
		
		private LinkedListIndexedCollection<T> kopija;
		
		/**
		 * represents pointer to the current ListNode of the class
		 */
		
		private ListNode<T> trenutni;
		
		/**
		 * Basic constructor
		 * @param lista Outer LinkedListIndexedCollection class instance
		 */
		
		public ListGetter(LinkedListIndexedCollection<T> lista) {
			kopija = lista;
			savedModificationCount = lista.modificationCount;
			trenutni = lista.first;
		}
		
		/**
		 * Method checks if current element exists
		 */
		
		public boolean hasNextElement() {
			if (savedModificationCount != kopija.modificationCount) throw new ConcurrentModificationException();
			if (trenutni!=null) return true;
			return false;
		}
		
		/**
		 * Method returns next element in the iteration
		 */
		
		public T getNextElement() {
			if (savedModificationCount != kopija.modificationCount) throw new ConcurrentModificationException();
			if (hasNextElement()) {
				if (trenutni.sljedeci!=null) {
					trenutni=trenutni.sljedeci;
					return (T) trenutni.prethodni.value;
				}else {
					T o = trenutni.value;
					trenutni=null;
					return o;
				}
			}
			throw new NoSuchElementException();
		}
		
		/**
		 * Method processes the remaining elements
		 */
		
		public void processRemaining(Processor<T> p) {
			
			while (trenutni!=null) {
				p.process(trenutni.value);
				trenutni=trenutni.sljedeci;
			}
			
		}
		
	}
	
	/**
	 * Method creates one ElementsGetter of the given class
	 */
	
	public ElementsGetter<T> createElementsGetter() {
		
		return new ListGetter<T>(this);
		
	}
	
	/**
	 * Method adds all elements from Collection col to the current Collection but only if they pass tester's test method.
	 */
	
	@SuppressWarnings("unchecked")
	public void addAllSatisfying(Collection<? extends T> col, Tester<? super T> tester) {
		
		ElementsGetter<T> eg = (ElementsGetter<T>) col.createElementsGetter();
		
		while (eg.hasNextElement()) {
			T o = eg.getNextElement();
			if (tester.test(o)) {
				add(o);
			}
		}
		
	}
	
	/**
	 * Method returns size of the Collection.
	 */
	
	
	public int size() {
		return size;
	}
	
	/**
	 * Method checks if Collection contains given Object value.
	 */
	
	public boolean contains(Object value) {
		if (value==null) throw new NullPointerException();
		
		ListNode<T> pomocni = first;
		
		for (int i=0;i<size;i++) {
			if (pomocni.value.equals(value)) return true;
			pomocni = pomocni.sljedeci;
		}
		return false;
	}
	
	/**
	 * Method converts current collection to array of Objects.
	 */
	
	public Object[] toArray() {
		
		Object[] result = new Object[size];
		
		ListNode<T> pomocni = first;
		
		for (int i=0;i<size;i++) {
			result[i] = pomocni.value;
			pomocni = pomocni.sljedeci;
		}
		return result;
	}
	
	/**
	 * Method removesgiven Object from the Collection.
	 */
	
	public boolean remove(Object value) {
		if (value==null) throw new NullPointerException();
		
		ListNode<T> pomocni = first;
		
		if (first.value.equals(value)) {
			first = pomocni.sljedeci;
			first.prethodni = null;
			modificationCount++;
			size--;
			return true;
		}
		if (last.value.equals(value)) {
			last = last.prethodni;
			last.sljedeci=null;
			modificationCount++;
			size--;
			return true;
		}
		
		for (int i=0;i<size;i++) {
			if (pomocni.value.equals(value)) {
				pomocni.prethodni.sljedeci = pomocni.sljedeci;
				pomocni.sljedeci.prethodni = pomocni.prethodni;
				modificationCount++;
				size--;
				return true;
			}
			pomocni = pomocni.sljedeci;
		}
		return false;
	}
	
	/**
	 * Method adds all elements from the given Collection other.
	 */
	
	public void addAll(Collection<? extends T> other) {
		
		class localProcessor implements Processor<T> {

            public void process(Object value) {
                add((T)value);
            }
        }

        other.forEach(new localProcessor());
		
	}
	
	/**
	 * Method checks if Collection is empty.
	 */
	
	public boolean isEmpty() {
		if (size()==0) return true;
		return false;
	}
	
	/**
	 * Method adds given Object to the Collection.
	 */
	
	public void add(T value) {
		if (value==null) throw new NullPointerException();
		
		if (first == null) {
            first = new ListNode<T>();
            first.value = value;
        } else if (last == null) {
            last = new ListNode<T>();
            last.value = value;
            first.sljedeci = last;
            last.prethodni = first;
        } else {
            last.sljedeci = new ListNode<T>();
            last.sljedeci.prethodni = last;
            last.sljedeci.value = value;
            last = last.sljedeci;

        }
		modificationCount++;
        size++;
	}
	
	/**
	 * returns the Object that is located at the given index in the list
	 * @param index position of the Object that we want to get
	 * @return Object on the given index in the list
	 */
	public T get(int index) {
		
		if (index<0 || index>=size) throw new IndexOutOfBoundsException();
		
		ListNode<T> pomocni;
		if (index <= size/2) {
			pomocni=first;
			for (int i=0;i<index;i++) {
				pomocni=pomocni.sljedeci;
			}
		}else {
			pomocni=last;
			for (int i=size-1;i>index;i--) {
				pomocni=pomocni.prethodni;
			}
		}
		return pomocni.value;
	}
	
	/**
	 * Method removes all elements from the Collection.
	 */
	
	public void clear() {
		first = null;
		last = null;
		modificationCount++;
		size = 0;
	}
	
	/**
	 * Average complexity of this method is n/2 +1
	 * inserts Object in the valid position and shifts all elements that have greater index to the right
	 * @param value value of the Object
	 * @param position index that we want Object to be inserted
	 */
	public void insert(T value, int position) {
		if (position<0 || position >size) throw new IndexOutOfBoundsException();
		if (value==null) throw new NullPointerException();
		if (first==null) {
			first = new ListNode<T>();
			last = first;
			first.value=value;
		}else if(position == size){
			ListNode<T> novi = new ListNode<>();
			last.sljedeci = novi;
			novi.prethodni = last;
			novi.value = value;
			last = novi;
		}else if(position==0) {
			ListNode<T> prvi = new ListNode<>();
			prvi.value = value;
			prvi.sljedeci = first;
			first = prvi;
		}else {
			ListNode<T> pomocni = new ListNode<>();
			ListNode<T> novi = new ListNode<>();
			for (int i=size-1;i>position;i--) {
				pomocni=last.prethodni;
			}
			pomocni.prethodni.sljedeci = novi;
			novi.prethodni = pomocni.prethodni;
			novi.sljedeci = pomocni;
			pomocni.prethodni = novi;
		}
		modificationCount++;
		size++;
	}
	
	
	/**
	 * Average complexity of this method is n/2 +1
	 * returns the index of the Object
	 * @param value value of the Object
	 * @return index of the first occurrence of the Object in the list. Returns -1 if Object wasn't found
	 */
	public int indexOf(Object value) {
		ListNode<T> pomocni = first;
		for (int i=0;i<size;i++) {
			if(pomocni.value.equals(value)) return i;
			pomocni = pomocni.sljedeci;
		}
		return -1;
	}
	
	/**
	 * removes the element from the given index
	 * @param index position of the element we want to remove
	 */
	public void remove(int index) {
		if (index<0 || index>=size) throw new IndexOutOfBoundsException();
		
		ListNode<T> pomocni = first;
		
		for (int i = 0; i < size; i++) {
            if (index == i) {
                pomocni.prethodni.sljedeci = pomocni.sljedeci;
                pomocni.sljedeci.prethodni = pomocni.prethodni;

            }
            pomocni = pomocni.sljedeci;

        }
		modificationCount++;
        size--;
	}
	
}

















































































