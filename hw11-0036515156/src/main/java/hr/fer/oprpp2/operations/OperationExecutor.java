package hr.fer.oprpp2.operations;

public class OperationExecutor {

	private OperationStrategy strategy;
	
	public OperationExecutor(OperationStrategy strategy) {
		this.strategy = strategy;
	}
	
	public Object executeOperation(Object obj1, Object obj2) {
		return strategy.doOperation(obj1, obj2);
	}
}
