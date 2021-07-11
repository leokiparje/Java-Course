package hr.fer.oprpp2.collections;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Collection sotres elements in a double linked list with reference to the first and last elements.
 * @author leokiparje
 *
 */
public class LinkedListIndexedCollection implements List {
	
	/**
	 * private class that represents a node 
	 * @author leokiparje
	 *
	 */
	private static class ListNode {
		
		/**
		 * ListNode of the previous node
		 */
		ListNode prethodni;
		
		/**
		 * ListNode of the next node
		 */
		ListNode sljedeci;
		
		/**
		 * Value of the object
		 */
		Object value;
	}
	
	/**
	 * size of the linked list
	 */
	private int size;
	
	/**
	 * reference to the first node of the list
	 */
	private ListNode first;
	
	/**
	 * reference of the last node of the list
	 */
	private ListNode last;
	
	private long modificationCount = 0;
	
	/**
	 * default constructor
	 */
	public LinkedListIndexedCollection() {
		
		size=0;
		first=null;
		last=null;
		
	}
	
	public LinkedListIndexedCollection(Collection other) {
		if (other==null) throw new NullPointerException();
		addAll(other);
		
	}
	
	private static class ListGetter implements ElementsGetter{
		
		private long savedModificationCount;
		private LinkedListIndexedCollection kopija;
		private ListNode trenutni;
		
		public ListGetter(LinkedListIndexedCollection lista) {
			kopija = lista;
			savedModificationCount = lista.modificationCount;
			trenutni = lista.first;
		}
		
		public boolean hasNextElement() {
			if (savedModificationCount != kopija.modificationCount) throw new ConcurrentModificationException();
			if (trenutni!=null) return true;
			return false;
		}
		
		public Object getNextElement() {
			if (savedModificationCount != kopija.modificationCount) throw new ConcurrentModificationException();
			if (hasNextElement()) {
				if (trenutni.sljedeci!=null) {
					trenutni=trenutni.sljedeci;
					return trenutni.prethodni.value;
				}else {
					Object o = trenutni.value;
					trenutni=null;
					return o;
				}
			}
			throw new NoSuchElementException();
		}
		
		public void processRemaining(Processor p) {
			
			while (trenutni!=null) {
				p.process(trenutni.value);
				trenutni=trenutni.sljedeci;
			}
			
		}
		
	}
	
	public ElementsGetter createElementsGetter() {
		
		return new ListGetter(this);
		
	}
	
	
	public void addAllSatisfying(Collection col, Tester tester) {
		
		ElementsGetter eg = (ElementsGetter) col.createElementsGetter();
		
		while (eg.hasNextElement()) {
			Object o = eg.getNextElement();
			if (tester.test(o)) add(o);
		}
		
	}
	
	
	public int size() {
		return size;
	}
	
	public boolean contains(Object value) {
		if (value==null) throw new NullPointerException();
		
		ListNode pomocni = first;
		
		for (int i=0;i<size;i++) {
			if (pomocni.value.equals(value)) return true;
			pomocni = pomocni.sljedeci;
		}
		return false;
	}
	
	public Object[] toArray() {
		
		Object[] result = new Object[size];
		
		ListNode pomocni = first;
		
		for (int i=0;i<size;i++) {
			result[i] = pomocni.value;
			pomocni = pomocni.sljedeci;
		}
		return result;
	}
	
	public boolean remove(Object value) {
		if (value==null) throw new NullPointerException();
		
		ListNode pomocni = first;
		
		if (first.value.equals(value)) {
			first = pomocni.sljedeci;
			first.prethodni = null;
			size--;
			return true;
		}
		if (last.value.equals(value)) {
			last = last.prethodni;
			last.sljedeci=null;
			size--;
			return true;
		}
		
		for (int i=0;i<size;i++) {
			if (pomocni.value.equals(value)) {
				pomocni.prethodni.sljedeci = pomocni.sljedeci;
				pomocni.sljedeci.prethodni = pomocni.prethodni;
				size--;
				return true;
			}
			pomocni = pomocni.sljedeci;
		}
		return false;
	}
	
	
	public void addAll(Collection other) {
		
		class localProcessor implements Processor {

            public void process(Object value) {
                add(value);
            }
        }

        other.forEach(new localProcessor());
		
	}
	
	public boolean isEmpty() {
		if (size()==0) return true;
		return false;
	}
	
	public void add(Object value) {
		if (value==null) throw new NullPointerException();
		
		if (first == null) {
            first = new ListNode();
            first.value = value;
        } else if (last == null) {
            last = new ListNode();
            last.value = value;
            first.sljedeci = last;
            last.prethodni = first;
        } else {
            last.sljedeci = new ListNode();
            last.sljedeci.prethodni = last;
            last.sljedeci.value = value;
            last = last.sljedeci;

        }
        size++;
	}
	
	/**
	 * returns the Object that is located at the given index in the list
	 * @param index position of the Object that we want to get
	 * @return Object on the given index in the list
	 */
	public Object get(int index) {
		
		ListNode pomocni;
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
	
	
	public void clear() {
		first = null;
		last = null;
		size = 0;
	}
	
	/**
	 * Average complexity of this method is n/2 +1
	 * inserts Object in the valid position and shifts all elements that have greater index to the right
	 * @param value value of the Object
	 * @param position index that we want Object to be inserted
	 */
	public void insert(Object value, int position) {
		if (position<0 || position >size) throw new IndexOutOfBoundsException();
		if (value==null) throw new NullPointerException();
		if (first==null) {
			first = new ListNode();
			last = first;
			first.value=value;
		}else if(position == size){
			ListNode novi = new ListNode();
			last.sljedeci = novi;
			novi.prethodni = last;
			novi.value = value;
			last = novi;
		}else {
			ListNode pomocni = new ListNode();
			ListNode novi = new ListNode();
			for (int i=size-1;i>position;i--) {
				pomocni=last.prethodni;
			}
			pomocni.prethodni.sljedeci = novi;
			novi.prethodni = pomocni.prethodni;
			novi.sljedeci = pomocni;
			pomocni.prethodni = novi;
		}
		size++;
	}
	
	
	/**
	 * Average complexity of this method is n/2 +1
	 * returns the index of the Object
	 * @param value value of the Object
	 * @return index of the first occurrence of the Object in the list. Returns -1 if Object wasn't found
	 */
	public int indexOf(Object value) {
		ListNode pomocni = first;
		for (int i=0;i<size;i++) {
			if(pomocni.value.equals(value)) return i;
		}
		return -1;
	}
	
	/**
	 * removes the element from the given index
	 * @param index position of the element we want to remove
	 */
	public void remove(int index) {
		if (index<0 || index>=size) throw new IndexOutOfBoundsException();
		
		ListNode pomocni = first;
		
		for (int i = 0; i < size; i++) {
            if (index == i) {
                pomocni.prethodni.sljedeci = pomocni.sljedeci;
                pomocni.sljedeci.prethodni = pomocni.prethodni;

            }
            pomocni = pomocni.sljedeci;

        }

        size--;
	}
	
	
}

















































































