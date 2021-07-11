package hr.fer.oprpp2.operations;

public class OperationSub implements OperationStrategy {

	@Override
	public Object doOperation(Object obj1, Object obj2) {
		if (obj1 instanceof Integer) {
			return Integer.valueOf((Integer)obj1-(Integer)obj2);
		}else {
			return Double.valueOf((Double)obj1-(Double)obj2);
		}
	}
}
