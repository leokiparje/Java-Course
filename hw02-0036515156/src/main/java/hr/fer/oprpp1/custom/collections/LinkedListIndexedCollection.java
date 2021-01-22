package hr.fer.oprpp1.custom.collections;

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
		
		public ListNode(Object value) {
			this.value=value;
		}
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
		
		ElementsGetter eg = col.createElementsGetter();
		
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
		
		if (size==1) {
			if (first.value.equals(value)) {
				first=null;
				last=null;
				modificationCount++;
				size--;
				return true;
			}
			return false;		
		}else if(size==2) {
			if (first.value.equals(value)) {
				first = last;
				first.prethodni=null;
				modificationCount++;
				size--;
				return true;
			}else if(last.value.equals(value)) {
				first.sljedeci=null;
				last=first;
				size--;
				modificationCount++;
				return true;
			}else {
				return false;
			}
		}else if (first.value.equals(value)) {
			first = first.sljedeci;
			first.prethodni = null;
			size--;
			modificationCount++;
			return true;
		}else if (last.value.equals(value)) {
			last = last.prethodni;
			last.sljedeci=null;
			size--;
			modificationCount++;
			return true;
		}else {		
			ListNode pomocni = first;
			for (int i=0;i<size;i++) {
				if (pomocni.value.equals(value)) {
					pomocni.prethodni.sljedeci = pomocni.sljedeci;
					pomocni.sljedeci.prethodni = pomocni.prethodni;
					size--;
					modificationCount++;
					return true;
				}
				pomocni = pomocni.sljedeci;
			}
			return false;			
		}
	}
	
	public void add(Object value) {
		if (value==null) throw new NullPointerException();
		
		ListNode novi = new ListNode(value);
		
		if (first == null) {
            first = novi;
            last = first;
        } else {
            last.sljedeci = novi;
            last.sljedeci.prethodni = last;
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
		modificationCount++;
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
		ListNode novi = new ListNode(value);
		if (size==0) {
			first = novi;
			last = novi;
		}else if (size==1) {
			if (position==0) {
				first = novi;
			}else {
				last = novi;
			}
			first.sljedeci = last;
			last.prethodni = first;
		}else if (position==0) {
			novi.sljedeci = first;
			first.prethodni = novi;
			first = novi;
		}else if(position==size) {
			last.sljedeci = novi;
			novi.prethodni = last;
			last = novi;
		}else {
			ListNode pomocni=first;
			for (int i=0;i<position;i++) {
				pomocni=pomocni.sljedeci;
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
		
		if (size==1) {
			first = null;
			last = null;
		}else if(size==2) {
			if (index==0) {
				last.prethodni=null;
				first = last;
			}else {
				first.sljedeci=null;
				last = first;
			}
		}else if(index==0) {
			first = first.sljedeci;
			first.prethodni = null;
		}else if(index==size-1) {
			last = last.prethodni;
			last.sljedeci=null;
		}else {
			ListNode pomocni = first;
			for (int i = 0; i < size; i++) {
	            if (index == i) {
	                pomocni.prethodni.sljedeci = pomocni.sljedeci;
	                pomocni.sljedeci.prethodni = pomocni.prethodni;

	            }
	            pomocni = pomocni.sljedeci;

	        }
		}
		modificationCount++;
        size--;
	}
	
	
}

















































































