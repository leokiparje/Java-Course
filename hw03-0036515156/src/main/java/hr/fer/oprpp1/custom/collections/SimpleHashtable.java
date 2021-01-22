package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import hr.fer.oprpp1.custom.collections.*;

/**
 * Class represents Hashtable which is iterable and internally stores array of TableEntry Objects.
 * @author leokiparje
 *
 * @param <K> type K of the Object key
 * @param <V> type V of the Object value
 */

public class SimpleHashtable<K,V> implements Iterable<SimpleHashtable.TableEntry<K, V>>{
	
	/**
	 * Internal representation of the Hashtable is array of TalbeEntry elements.
	 */
	
	TableEntry<K,V>[] table;
	
	/**
	 * Int value of current number of elements in hashtable.
	 */
	
	int size;
	
	/**
	 * Int value of current modification count.
	 */
	
	private int modificationCount=0;
	
	/**
	 * Default constructor.
	 */
	
	public SimpleHashtable() {
		this(16);
	}
	
	/**
	 * Basic constructor.
	 * @param initialCapacity
	 */
	
	public SimpleHashtable(int initialCapacity) {
		if (initialCapacity<1) throw new IllegalArgumentException();
		else table = (TableEntry<K,V>[])new TableEntry[((int) Math.ceil(Math.sqrt(initialCapacity)))];
		size = 0;
	}
	
	/**
	 * Class TableEntry represents one element in Hashtable's internal array.
	 * @author leokiparje
	 *
	 * @param <K> K type of the Object key
	 * @param <V> V type of the Object vaue
	 */
	
	public static class TableEntry<K,V> {
		
		/**
		 * K key
		 */
		
		private K key;
		
		/**
		 * V value
		 */
		
		private V value;
		
		/**
		 * Pointer to the next TableEntry
		 */
		
		private TableEntry<K,V> next;
		
		/**
		 * Basic constructor
		 * @param key K key
		 * @param value V value
		 */
		
		public TableEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * Getter for key
		 * @return K key
		 */
		
		public K getKey() { return key; }
		
		/**
		 * Getter for value
		 * @return V value
		 */

		public V getValue() { return value; }
		
		/**
		 * Setter for value
		 * @param value new value that will be set.
		 */
		
		public void setValue(V value) { this.value = value; }
		
		public String toString() {
			
			return key + "->" + value;
			
		}
		
	}
	
	/**
	 * Method returns size.
	 * @return
	 */
	
	public int size() {
		
		return size;
		
	}
	
	/**
	 * Method removes all elements from the given Hashtable.
	 */
	
	public void clear() {
		
		for (int i=0;i<table.length;i++) table[i] = null; 
		size = 0;
		modificationCount++;
		
	}
	
	/**
	 * Method puts given TableEntry to the Hashtable.
	 */
	
	public V put(K key, V value) {
		
		if (key==null) throw new NullPointerException();
		
		
		
		if (!containsKey(key) && (Double.compare((double)size()/table.length, (double)0.75) == 0 || size()/table.length>0.75)) {
			
			TableEntry<K,V>[] tableArray = toArray();	
			table = (TableEntry<K,V>[])new TableEntry[table.length*2];
			modificationCount++;
			
			int index;
			
			for (int i=0;i<tableArray.length;i++) {
				
				index = Math.abs(tableArray[i].getKey().hashCode())%table.length;
				
				if (table[index]==null) {
					
					table[index] = new TableEntry<K,V>(tableArray[i].getKey(),tableArray[i].getValue());
					continue;
					
				}
				
				TableEntry<K,V> dodatni = table[index];
				
				while(true) {
					if (dodatni.next==null) break;
					dodatni=dodatni.next;
				}
				dodatni.next = new TableEntry<K,V>(tableArray[i].getKey(),tableArray[i].getValue());	
			}
		}
		
		
		
		int index = Math.abs(key.hashCode())%table.length;
		
		if (table[index]!=null) {
			TableEntry<K,V> pomocni = table[index];
			while(true) {
				if (pomocni.key.equals(key)) {
					V result = pomocni.value;
					pomocni.value=value;
					return result;
				}
				if (pomocni.next==null) break;
				pomocni = pomocni.next;
			}
			pomocni.next = new TableEntry<K,V>(key, value);
			size++;
			modificationCount++;
			return null;
		}
		
		table[index] = new TableEntry<K,V>(key,value);
		size++;
		modificationCount++;
		return null;
		
	}
	
	/**
	 * Method returns value of the given key from the Hashtable.
	 * @param key K key
	 * @return V value
	 */
	
	public V get (Object key) {
		
		int index = Math.abs(key.hashCode())%table.length;
		
		if (key==null || table[index]==null) return null;
		
		TableEntry<K,V> pomocni = table[index];
		
		while (true) {
			
			if (pomocni.key.equals(key)) return pomocni.value;
			if (pomocni.next==null) break;
			pomocni = pomocni.next;
			
		}
		
		return null;
		
	}
	
	/**
	 * Method removes TableEntry with given key.
	 * @param key K key
	 * @return V value
	 */
	
	public V remove(Object key) {
		
		if (key==null) return null;
		
		int index = Math.abs(key.hashCode())%table.length;
		
		if (table[index]==null) return null;
		
		TableEntry<K,V> pomocni = table[index];
		
		TableEntry<K,V> prethodni = null;
		
		V result;
		
		if (pomocni.key.equals(key)) {
			result = table[index].value;
			table[index] = pomocni.next;
			size--;
			modificationCount++;
			return result;
		}
		
		while (true) {
			
			if (pomocni.key.equals(key)) {
				
				if (pomocni.next!=null) {
					
					result = pomocni.value;
					prethodni.next = pomocni.next;
					modificationCount++;
					size--;
					return result;
					
				}
				
				prethodni.next = null;
				modificationCount++;
				size--;
				return pomocni.value;
				
			}
			if (pomocni.next==null) break;
			prethodni = pomocni;
			pomocni = pomocni.next;
			
		}
		
		return null;
		
	}
	
	/**
	 * Methods checks if the given key is contained inside the Hashtable.
	 * @param key K key
	 * @return boolean true or false
	 */
	
	public boolean containsKey(Object key) {
		
		if (key==null) return false;
		
		int index = Math.abs(key.hashCode())%table.length;
		
		if (table[index]==null) return false;
		
		TableEntry<K,V> pomocni = table[index];
		
		while(true) {
			
			if (pomocni.key.equals(key)) return true;
			if (pomocni.next==null) break;
			pomocni = pomocni.next;
			
		}
		
		return false;
		
	}
	
	/**
	 * Method checks if the given value is contained inside the Hashtable.
	 * @param value V value
	 * @return boolean true or false
	 */
	
	public boolean containsValue(Object value) {
		
		for (int i=0;i<table.length;i++) {
			
			TableEntry<K,V> pomocni = table[i];
			
			while(true) {
				
				if (pomocni==null) break;
				if (table[i].value.equals(value)) return true;
				pomocni = pomocni.next;
				
			}
			
		}
		return false;
		
	}
	
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		for (int i=0;i<table.length;i++) {
			
			TableEntry<K,V> pomocni = table[i];
			
			while(true) {
				
				if (pomocni==null) break;
				sb.append(pomocni.key);
				sb.append("=");
				sb.append(pomocni.value);
				sb.append(", ");
				
				pomocni=pomocni.next;
				
			}
			
		}
		
		if (sb.length()<2) {
			sb.append("]");
			return sb.toString();
		}
		
		sb.setLength(sb.length()-2);
		sb.append("]");
		return sb.toString();
		
	}
	
	/**
	 * Method converts given Hashtable to array.
	 * @return Newly created array of TableEntries.
	 */
	
	public TableEntry<K,V>[] toArray() {
		
		TableEntry<K,V>[] tabela = (TableEntry<K,V>[])new TableEntry[size()];
		int index=0;
		
		for (int i=0;i<table.length;i++) {
			
			TableEntry<K,V> pomocni = table[i];
			
			while(true) {
				
				if (pomocni==null) break;
				tabela[index++] = pomocni;
				pomocni = pomocni.next;
				
			}
			
		}
		
		return tabela;
		
	}
	
	/**
	 * Class IteratorImpl represents iterator which will be used for iterating.
	 * @author leokiparje
	 *
	 */
	
	
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K,V>> {
		
		/**
		 * Pointer to current TableEntry.
		 */
		
		TableEntry<K,V> current;
		
		/**
		 * Int value of current Index in table.
		 */
		
		int indexTable;
		
		/**
		 * Boolean value that represents if the Object is removed after the next() method is called.
		 */
		
		boolean isRemoved = false;
		
		/**
		 * Int value that represents the current modification count number.
		 */
		
		private int currentMC;
		
		/**
		 * Default constructor.
		 */
		
		public IteratorImpl() {
			for (int i=0;i<table.length;i++) {
				if (table[i]==null) continue;
				current = table[i];
				indexTable = i;
				currentMC = modificationCount;
				return;
			}
			current = null;
		}

		@Override
		public boolean hasNext() {
			if (currentMC!=modificationCount) throw new ConcurrentModificationException();
			return current!=null;
		}

		@Override
		public TableEntry<K, V> next() {
			
			if (hasNext()) {
				
				isRemoved = false;
				
				TableEntry<K,V> result = current;
				
				if (current.next!=null) {
					
					current = current.next;
					return result;
					
				}else {
					
					if (indexTable==table.length-1) {
						current = null;
						return result;
					}
					
					for (int i=indexTable+1;i<table.length;i++) {
						if (table[i]!=null) {
							current = table[i];
							indexTable = i;
							return result;
						}
					}
					current = null;
					return result;
				}
			}
			throw new NoSuchElementException();
		}
		
		@Override
		public void remove() {
			
			if (currentMC!=modificationCount) throw new ConcurrentModificationException();
			
			if (!isRemoved) {
				
				isRemoved = true;
				
				TableEntry<K,V> pomocni = table[indexTable];
				TableEntry<K,V> prethodni = null;
				
				if (current==null) {
					for (int i=table.length-1;i>=0;i--) {
						if (table[i]!=null) {
							SimpleHashtable.this.remove(table[i].getKey());
							currentMC++;
							return;
						}
					}
					throw new IllegalStateException();
				}
				
				if (current!=table[indexTable]) {
					while(true) {
						prethodni = pomocni;
						pomocni = pomocni.next;
						//if (pomocni.next==null) break;
						if (pomocni==current) break;
					}
					
					SimpleHashtable.this.remove(prethodni.getKey());
					currentMC++;
					return;
				}
				
				for (int i=indexTable-1;i>=0;i--) {
					
					if (table[i]==null) continue;
					pomocni = table[i];
					while(true) {
						if (pomocni.next==null) break;
						pomocni=pomocni.next;
					}
					SimpleHashtable.this.remove(pomocni.getKey());
					currentMC++;
					return;
					
				}
				throw new IllegalStateException();
				
				
			}
			throw new IllegalStateException();
			
		}
		
	}
	
	@Override
	public Iterator<SimpleHashtable.TableEntry<K, V>> iterator() {
		
		return new IteratorImpl();
		
	}
}

































































