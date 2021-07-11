package hr.fer.oprpp2.scripting.exec;

import java.util.HashMap;
import java.util.Map;

public class ObjectMultistack {
	
	private Map<String,MultistackEntry> stringToStack;
	
	public ObjectMultistack() {
		this.stringToStack = new HashMap<>();
	}
	
	private static class MultistackEntry {
		
		public MultistackEntry(ValueWrapper value) {
			this.value = value;
		}
		
		public MultistackEntry(ValueWrapper value, MultistackEntry next) {
			this.value = value;
			this.next = next;
		}
		
		private ValueWrapper value;
		private MultistackEntry next;
		
	}

	public void push(String keyName, ValueWrapper valueWrapper) {
		if (stringToStack.containsKey(keyName)) {
			MultistackEntry me = stringToStack.get(keyName);
			MultistackEntry newMe = new MultistackEntry(valueWrapper,me);
			stringToStack.put(keyName,newMe);
		}else {
			stringToStack.put(keyName,new MultistackEntry(valueWrapper));
		}
	}
	
	public ValueWrapper pop(String keyName) {
		if (!stringToStack.containsKey(keyName)) throw new RuntimeException("Trying to pop from non existing stack.");
		ValueWrapper popped = stringToStack.get(keyName).value;
		stringToStack.put(keyName,stringToStack.get(keyName).next);
		return popped;
	}
	
	public ValueWrapper peek(String keyName) {
		if (!stringToStack.containsKey(keyName)) throw new RuntimeException("Trying to peek from non existing stack.");
		return stringToStack.get(keyName).value;
	}
	
	public boolean isEmpty(String keyName) {
		if (stringToStack.containsKey(keyName)) return true;
		return false;
	}
}
