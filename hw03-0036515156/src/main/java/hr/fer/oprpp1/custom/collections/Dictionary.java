package hr.fer.oprpp1.custom.collections;

/**
 * Class Dictionary represents Collection with key and value.
 * @author leokiparje
 *
 * @param <K> Object of type K represents key
 * @param <V> Object of type V represents value
 */

public class Dictionary<K,V> {
	
	/**
	 * dictionary is a ArrayIndexedCollection instance in which we will store Pair elements.
	 */
	
	private ArrayIndexedCollection<Pair<K,V>> dictionary;
	
	/**
	 * Basic constructor
	 */
	
	public Dictionary(){
		
		dictionary = new ArrayIndexedCollection<Pair<K,V>>();
		
	}
	
	/**
	 * Private Inner class Pair represents one pair that will be stored inside of dictionary.
	 * @author leokiparje
	 *
	 * @param <K> Object of type K represents key
	 * @param <V> Object of type V represents value
	 */
	
	private static class Pair<K,V> {
		
		/**
		 * Object of type K represents key.
		 */
		
		private K key;
		
		/**
		 * Object of type V represents value.
		 */
		
		private V value;
		
		/**
		 * Basic constructor
		 * @param key Object of type K represents key
		 * @param value Object of type V represents value
		 */
		
		public Pair(K key, V value) {
			
			if (key==null) throw new NullPointerException();
			
			this.key=key;
			this.value=value;
			
		}
		
		/**
		 * Basic constructor
		 */
		
		public Pair() {
			
			this.key=null;
			this.value=null;
			
		}
		
		public String toString() {
			return key + "->" + value;
		}
		
		public boolean equals(Object other) {
			if (!(other instanceof Pair)) return false;
			@SuppressWarnings("unchecked")
			Pair<K,V> p = (Pair<K,V>) other;
			if (this.key.equals(p.key) && this.value.equals(p.value)) return true;
			return false;
		}
		
	}
	
	/**
	 * Method checks if dictionary is empty
	 * @return boolean true or false
	 */
	
	public boolean isEmpty() {
		
		if (size()==0) return true;
		return false;
		
	}
	
	/**
	 * Method checks how many elements are in dictionary
	 * @return
	 */
	
	public int size() {
		
		return dictionary.size();
		
	}
	
	/**
	 * Method removes all elements from the dictionaty.
	 */
	
	public void clear() {
		
		dictionary.clear();
		
	}
	
	/**
	 * Method puts given Pair to the dictionary
	 * @param key Object of type K
	 * @param value Object of type V
	 * @return returns old value of element with given key, or if the given key doesn't exist in current dictionary it returns null
	 */
	
	public V put(K key, V value) {
		
		for (int i=0;i<size();i++) {
			if (dictionary.get(i).key.equals(key)) {
				V result = dictionary.get(i).value;
				dictionary.get(i).value=value;
				return result;
			}
		}
		dictionary.insert(new Pair<K,V>(key,value), size());
		return null;
		
	}
	
	/**
	 * Method returns value of Object which key is given
	 * @param key Object of type K
	 * @return value of type V of the Pair with key K
	 */
	
	public V get(Object key) {
		
		for (int i=0;i<size();i++) {
			
			if (dictionary.get(i).key.equals(key)) return dictionary.get(i).value;
			
		}
		
		return null;
		
	}
	
	/**
	 * Method removes element with given key in the dictionary.
	 * @param key Object of type K
	 * @return value of given key
	 */
	
	public V remove(K key) {
		
		if (get(key)==null) return null;
		V v = get(key);
		dictionary.remove(new Pair<K,V>(key, v));
		return v;
		
	}
	
}


















































































































