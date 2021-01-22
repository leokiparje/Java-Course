package hr.fer.zemris.lsystems.impl;

import hr.fer.oprpp1.custom.collections.ObjectStack;

public class Context {

	ObjectStack<TurtleState> stack;
	
	public Context() {
		stack = new ObjectStack<>();
	}
	
	public TurtleState getCurrentState() { 
		return stack.peek();	
	}
	
	public void pushState(TurtleState state) {
		stack.push(state);
	}
	
	public void popState(){
		stack.pop();
	}
	
}
