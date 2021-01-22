package hr.fer.oprpp1.custom.collections;

/**
 * public interface extends collection
 * @author leokiparje
 *
 */

public interface List extends Collection{
	Object get(int index);
	void insert(Object value, int position);
	int indexOf(Object value);
	void remove(int index);
}
