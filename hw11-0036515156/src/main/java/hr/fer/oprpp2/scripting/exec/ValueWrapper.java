package hr.fer.oprpp2.scripting.exec;

import hr.fer.oprpp2.operations.Operation;
import hr.fer.oprpp2.operations.OperationAdd;
import hr.fer.oprpp2.operations.OperationDiv;
import hr.fer.oprpp2.operations.OperationExecutor;
import hr.fer.oprpp2.operations.OperationMul;
import hr.fer.oprpp2.operations.OperationSub;

public class ValueWrapper {

	private Object object;
	
	public ValueWrapper(Object object) {
		this.object = object;
	}

	public Object getValue() {
		return object;
	}
	
	public double getDoubleValue() {
		if (object==null) {
			return 0;
		}else if (object instanceof Double) {
			return (double)object;
		}else if(object instanceof Integer) {
			return (double) (Integer) object;
		}else if(object instanceof String) {
			String string = (String) object;
            try {
                if (string.contains(".") || string.contains("E")) {
                    return Double.parseDouble(string);
                } else {
                    return (double) Integer.parseInt(string);
                }
            } catch (NumberFormatException ex) {
                throw new RuntimeException("Number cannot be created from string.");
            }
		}else throw new RuntimeException("Fatal error. Impossible state.");
	}

	public void setValue(Object object) {
		this.object = object;
	}
	
	public void add(Object incValue) {
		try {
			doOperation(incValue,Operation.ADD);
		}catch(Exception e) {
			System.out.println("Error while adding two objects.");
			e.printStackTrace();
		}
	}
	
	public void subtract(Object decValue) {
		try {
			doOperation(decValue,Operation.SUB);
		}catch(Exception e) {
			System.out.println("Error while subtracting two objects.");
			e.printStackTrace();
		}
	}
	
	public void multiply(Object mulValue) {
		try {
			doOperation(mulValue,Operation.MUL);
		}catch(Exception e) {
			System.out.println("Error while multiplying two objects.");
			e.printStackTrace();
		}
	}
	
	public void divide(Object divValue) {
		try {
			doOperation(divValue,Operation.DIV);
		}catch(Exception e) {
			System.out.println("Error while dividing two objects.");
			e.printStackTrace();
		}
	}
	
	private void doOperation(Object otherObject, Operation operationType) {
		checkObjectType(otherObject);
		String orgObject = this.object==null ? "0" : this.object.toString();
		String incObject = otherObject==null ? "0" : otherObject.toString();
		
		OperationExecutor executor = null;
		switch(operationType) {
		case ADD: executor = new OperationExecutor(new OperationAdd()); break;
		case SUB:	executor = new OperationExecutor(new OperationSub()); break;
		case MUL: executor = new OperationExecutor(new OperationMul()); break;
		case DIV: executor = new OperationExecutor(new OperationDiv()); break;
		}
		
		if(bothIntegers(orgObject,incObject)) {
			this.object = Integer.parseInt(orgObject);
			Integer i = Integer.parseInt(incObject);
			this.object = executor.executeOperation(this.object,i);
		}else {
			this.object = Double.parseDouble(orgObject);
			Double d = Double.parseDouble(incObject);
			this.object = executor.executeOperation(this.object,d);
		}
	}
	
	public int numCompare(Object withValue) {
		try {
			checkObjectType(withValue);
			String orgObject = this.object==null ? "0" : this.object.toString();
			String incObject = withValue==null ? "0" : withValue.toString();
			
			if(bothIntegers(orgObject,incObject)) {
				int i1 = Integer.parseInt(orgObject);
				int i2 = Integer.parseInt(incObject);
				return Integer.compare(i1,i2);
			}else {
				double d1 = Double.parseDouble(orgObject);
				Double d2 = Double.parseDouble(incObject);
				return Double.compare(d1,d2);
			}
			
		}catch(Exception e) {
			System.out.println("Error while comparing two objects.");
			e.printStackTrace();
		}
		throw new RuntimeException("Fatal error. Impossible state.");
	}
	
	private boolean bothIntegers(String s1, String s2) {
		if (s1.contains(".") || s1.contains("E") || s2.contains(".") || s2.contains("E")) return false;
		return true;
	}
	
	private void checkObjectType(Object object) {
		if (!(this.object==null || this.object instanceof String || this.object instanceof Integer || this.object instanceof Double)) {
			throw new RuntimeException("Value wrapper contains value that is not compatible.");
		}
		if (!(object==null || object instanceof String || object instanceof Integer || object instanceof Double)) {
			throw new RuntimeException("ValueWrapper can only contain string, integer, double or null values.");
		}
	}
}
