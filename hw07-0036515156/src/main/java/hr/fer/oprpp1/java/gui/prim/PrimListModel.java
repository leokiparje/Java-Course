package hr.fer.oprpp1.java.gui.prim;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Class PrimListModel represents a list of prime numbers which are to be displayed on the prime model frame.
 * @author leokiparje
 *
 * @param <T> value T 
 */

public class PrimListModel<T> implements ListModel<T> {
	
	private List<T> brojevi = new ArrayList<>();
	private int prime = 0;
	private List<ListDataListener> promatraci = new ArrayList<>();
	
	public int next() {
		while(true) {
			if (isPrime(++prime)) {
				return prime;
			}
		}
	}

	@Override
	public int getSize() {
		return brojevi.size();
	}

	@Override
	public T getElementAt(int index) {
		return brojevi.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		promatraci.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		promatraci.remove(l);
		
	}
	
	public void remove(int pos) {
		brojevi.remove(pos);
		ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, pos, pos);
		for(ListDataListener l : promatraci) {
			l.intervalRemoved(event);
		}
	}
	
	public void add(T element) {
		int pos = brojevi.size();
		brojevi.add(element);
		
		ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, pos, pos);
		for(ListDataListener l : promatraci) {
			l.intervalAdded(event);
		}
	}
	
	public boolean isPrime(int num) {
		   if (num < 1) {
		       return false;
		   }
		   for (int i = 2; i <= Math.sqrt(num); i++) {
		       if (num % i == 0) {
		           return false;
		       }
		   }
		   return true;
	}
	
}
