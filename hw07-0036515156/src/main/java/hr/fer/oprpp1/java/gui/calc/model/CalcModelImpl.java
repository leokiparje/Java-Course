package hr.fer.oprpp1.java.gui.calc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

/**
 * Simple class that implements CalcModel interface. It's used as a model for calculator program.
 * @author leokiparje
 *
 */

public class CalcModelImpl implements CalcModel {
	
	boolean clickedOnEmptyStack = false;
	
	boolean editable = true;
	
	boolean positive = true;
	
	private String numbers = "";
	
	double number = 0.;
	
	private String frozen = null;
	
	private Double activeOperand;
	
	private DoubleBinaryOperator pendingOperation;
	
	private List<CalcValueListener> listListeners = new ArrayList<>();
	
	@Override
	public void addCalcValueListener(CalcValueListener l) {
		listListeners.add(l);
	}

	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		listListeners.remove(l);
	}

	@Override
	public double getValue() {
		return positive ? number : number*-1;
	}

	@Override
	public void setValue(double value) {
		number = value;
		numbers = ""+value;
		frozen = null;
		editable = false;
		valueChanged();
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	@Override
	public void clear() {
		positive = true;
		number = 0.;
		numbers = "";
		editable = true;
		valueChanged();
	}

	@Override
	public void clearAll() {
		clear();
		frozen = null;
		activeOperand = null;
		pendingOperation = null;
		valueChanged();
	}

	@Override
	public void swapSign() throws CalculatorInputException {
		
		if (!editable) throw new CalculatorInputException("Kalkulator je u modu gdje ne dozvoljava promjene.");
		
		positive = !positive;
		
		valueChanged();
		
	}

	@Override
	public void insertDecimalPoint() throws CalculatorInputException {
		
		if (!editable) throw new CalculatorInputException("Kalkulator je u modu gdje ne dozvoljava promjene.");
		
		if (numbers.indexOf(".")!=-1) throw new CalculatorInputException();
		
		if (numbers.length()==0) throw new CalculatorInputException("Decimal dot is only writable after input of a number.");
		
		numbers += ".";
		
		valueChanged();
		
	}

	@Override
	public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
		
		if (!editable) {
			throw new CalculatorInputException("Kalkulator u trenutnom stanju ne dozvoljava unos podataka.");
		}
		
		try {
			frozen = null;
			number = Double.parseDouble(numbers+digit);
			
			if (Double.isInfinite(number)) throw new CalculatorInputException("Number is too big.");
			
			if (!numbers.equals("0")) {
				numbers += digit;
			}else {
				if (digit!=0) {
					numbers = ""+digit;
				}
			}
			
		}catch(Exception e) {
			throw new CalculatorInputException("Pogreška prilikom unosa znamenke.");
		}
		
		valueChanged();
		
	}

	@Override
	public boolean isActiveOperandSet() {
		return !(activeOperand==null);
	}

	@Override
	public double getActiveOperand() throws IllegalStateException {
		
		if (!isActiveOperandSet()) throw new IllegalStateException("Active operand isn't set.");
		
		return activeOperand;
	}

	@Override
	public void setActiveOperand(double activeOperand) {
		
		this.activeOperand = activeOperand;
		
	}

	@Override
	public void clearActiveOperand() {
		activeOperand = null;
	}

	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return pendingOperation;
	}

	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		pendingOperation = op;
	}
	
	public void valueChanged() {
		for (CalcValueListener listener : listListeners) {
			listener.valueChanged(this);
		}
	}

	public String toString() {
		
		if (clickedOnEmptyStack) {
			clickedOnEmptyStack = false;
			return "Stack is empty ";
		}
		
		if (frozen!=null) return frozen;
		
		if (numbers.length()==0) return positive ? "0" : "-0";	
		
		return positive ? numbers : "-"+numbers;
		
	}

	@Override
	public void freezeValue(String value) {
		frozen = value;
	}

	@Override
	public boolean hasFrozenValue() {
		return frozen!=null;
	}
	
	public void emptyStack() {
		clickedOnEmptyStack = true;
		valueChanged();
	}
	
	public void setNumbers(String text) {
		this.numbers = text;
		valueChanged();
	}
}













































































