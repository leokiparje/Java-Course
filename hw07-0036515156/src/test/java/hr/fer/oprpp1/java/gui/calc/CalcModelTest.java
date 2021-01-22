package hr.fer.oprpp1.java.gui.calc;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hr.fer.oprpp1.java.gui.calc.model.CalcModel;
import hr.fer.oprpp1.java.gui.calc.model.CalcValueListener;
import hr.fer.oprpp1.java.gui.calc.model.CalculatorInputException;

import java.util.function.DoubleBinaryOperator;

public class CalcModelTest {
	
	private CalcModel model;
	
	private static CalcModel newCalcModel() {
		// Zamijenite ovo tako da vraća primjerak Vaše implementacije modela.
		return new DummyCalcModel();
	}

	@BeforeEach
	public void setup() {
		model = newCalcModel();
	}

	@Test
	public void valueOfNewModel() {
		assertEquals(0.0, model.getValue(), 1E-10); 
	}
	
	@Test
	public void toStringOfNewModel() {
		assertEquals("0", model.toString()); 
	}

	@Test
	public void newModelIsEditable() {
		assertTrue(model.isEditable()); 
	}

	@Test
	public void valueOfNewModelAfterSignSwap() {
		model.swapSign();
		
		assertEquals(0.0, model.getValue(), 1E-10); 
	}
	
	@Test
	public void toStringOfNewModelAfterSignSwap() {
		model.swapSign();
		
		assertEquals("-0", model.toString()); 
	}

	@Test
	public void pointWhenNoNumberThrows() {
		assertThrows(CalculatorInputException.class, ()->{
			model.insertDecimalPoint();
		});
	}

	@Test
	public void pointWhenNoNumberAndNegativeSignThrows() {
		model.swapSign();

		assertThrows(CalculatorInputException.class, ()->{model.insertDecimalPoint();});
	}

	@Test
	public void setSimpleValue() {
		model.setValue(-3.14);

		assertEquals(-3.14, model.getValue(), 1E-10); 
	}

	@Test
	public void toStringAfterSetSimpleValue() {
		model.setValue(-3.14);

		assertEquals("-3.14", model.toString());
	}

	@Test
	public void afterSetValueModelIsNotEditable() {
		model.setValue(-3.14);

		assertFalse(model.isEditable());
	}

	@Test
	public void setInfinityValue() {
		model.setValue(Double.POSITIVE_INFINITY);

		assertEquals(Double.POSITIVE_INFINITY, model.getValue()); 
	}

	@Test
	public void toStringAfterSetInfinityValue() {
		model.setValue(Double.POSITIVE_INFINITY);

		assertEquals("Infinity", model.toString()); 
	}

	@Test
	public void toStringAfterSetNegativeInfinityValue() {
		model.setValue(Double.NEGATIVE_INFINITY);

		assertEquals("-Infinity", model.toString()); 
	}

	@Test
	public void enterWholeNumber() {
		model.insertDigit(1);
		model.insertDigit(1);
		model.insertDigit(9);

		assertEquals(119, model.getValue(), 1E-10); 
		assertEquals("119", model.toString()); 
	}

	@Test
	public void enterDecimalNumber() {
		model.insertDigit(1);
		model.insertDigit(1);
		model.insertDigit(9);
		model.insertDecimalPoint();
		model.insertDigit(3);
		model.insertDigit(2);

		assertEquals(119.32, model.getValue(), 1E-10); 
		assertEquals("119.32", model.toString()); 
	}

	@Test
	public void checkDecimalNumberWithZeroWhole() {
		model.insertDigit(0);
		model.insertDecimalPoint();
		model.insertDigit(0);
		model.insertDigit(3);
		model.insertDigit(0);
		model.insertDigit(2);

		assertEquals(0.0302, model.getValue(), 1E-10); 
		assertEquals("0.0302", model.toString());
	}

	@Test
	public void signHasEffectOnZeroValue() {
		model.insertDigit(0);
		model.swapSign();

		assertEquals(0.0, model.getValue(), 1E-10); 
		assertEquals("-0", model.toString()); 
	}

	@Test
	public void whenSwapSingIsLast() {
		model.insertDigit(5);
		model.swapSign();

		assertEquals(-5, model.getValue(), 1E-10); 
		assertEquals("-5", model.toString()); 
	}

	@Test
	public void evenNumberOfSwapSignHasNoEffect() {
		model.insertDigit(5);
		model.swapSign();
		model.swapSign();
		model.swapSign();
		model.swapSign();

		assertEquals(5.0, model.getValue(), 1E-10); 
		assertEquals("5", model.toString()); 
	}

	@Test
	public void swapSignWorkAtAnyTime() {
		model.insertDigit(0);
		model.insertDecimalPoint();
		model.insertDigit(3);
		model.swapSign();
		model.insertDigit(2);

		assertEquals(-0.32, model.getValue(), 1E-10); 
		assertEquals("-0.32", model.toString()); 
	}

	@Test
	public void multipleInsertDecimalPointThrowsException() {
		model.insertDigit(4);
		model.insertDecimalPoint();
		model.insertDigit(3);
		
		assertThrows(CalculatorInputException.class, ()->{
			model.insertDecimalPoint();
		});
	}

	// Pokušaj dodavanja nove znamenke kojom bi broj postao prevelik
	// za konačnu reprezentaciju u double-tipu se ne dopušta već baca
	// iznimku!
	@Test
	public void safeWithTooBigNumbers() {
		for(int i = 1; i <= 308; i++) {
			model.insertDigit(9);
		}

		assertThrows(CalculatorInputException.class, ()->{
			model.insertDigit(9);
		});
	}
	
	@Test()
	public void readingActiveOperandWhenNotSet() {
		assertThrows(IllegalStateException.class, ()->{
			model.getActiveOperand();
		});
	}
	
	@Test
	public void setActiveOperand() {
		double value = 42.0;
		model.setActiveOperand(value);
		assertTrue(model.isActiveOperandSet());
		assertEquals(42.0, model.getActiveOperand(), 1E-10); 
	}

	@Test
	public void afterClearActiveOperandActiveOperandIsNotSet() {
		model.setActiveOperand(42);
		model.clearActiveOperand();
		assertFalse(model.isActiveOperandSet());
	}
	
	@Test
	public void activeOperandIsInitiallyNotSet() {
		assertFalse(model.isActiveOperandSet());
	}

	@Test
	public void afterClearAllActiveOperandIsNotSet() {
		model.setActiveOperand(42);
		model.clearAll();
		assertFalse(model.isActiveOperandSet());
	}
	
	@Test
	public void afterClearActiveOperandRemainsSet() {
		model.setActiveOperand(42);
		model.clear();
		assertTrue(model.isActiveOperandSet());
	}

	@Test
	public void multipleZerosStartingNumberAreIgnored() {
		model.insertDigit(0);
		model.insertDigit(0);
		model.insertDigit(0);
		model.insertDigit(0);
		
		assertEquals(0.0, model.getValue(), 1E-10); 
		assertEquals("0", model.toString()); 
	}

	@Test
	public void leadingZerosAreIgnored() {
		model.insertDigit(0);
		model.insertDigit(0);
		model.insertDigit(3);
		model.insertDigit(4);
		
		assertEquals(34.0, model.getValue(), 1E-10); 
		assertEquals("34", model.toString()); 
	}

	@Test
	public void exampleFromHomeworkAssignment() {
		model.insertDigit(5);
		model.insertDigit(8);
		model.setActiveOperand(model.getValue());
		model.setPendingBinaryOperation(Double::sum);
		model.clear();
		model.insertDigit(1);
		model.insertDigit(4);
		
		double result = model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue());
		model.setValue(result);
		model.clearActiveOperand();
		model.setPendingBinaryOperation(null);
		
		assertEquals(72.0, model.getValue(), 1E-10); 
		assertEquals("72.0", model.toString()); 
	}

	private static class DummyCalcModel implements CalcModel {
		
		boolean editable = true;
		
		boolean positive = true;
		
		String numbers = "";
		
		double number = 0.;
		
		String frozen = null;
		
		Double activeOperand;
		
		DoubleBinaryOperator pendingOperation;

		@Override
		public void addCalcValueListener(CalcValueListener l) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeCalcValueListener(CalcValueListener l) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public double getValue() {
			return positive ? number : number*-1;
		}

		@Override
		public void setValue(double value) {
			number = value;
			frozen = ""+value;
			editable = false;
		}

		@Override
		public boolean isEditable() {
			return editable;
		}

		@Override
		public void clear() {
			number = 0.;
			numbers = "";
		}

		@Override
		public void clearAll() {
			number = 0.;
			numbers = null;
			activeOperand = null;
			pendingOperation = null;
		}

		@Override
		public void swapSign() throws CalculatorInputException {
			
			if (!editable) throw new CalculatorInputException("Kalkulator je u modu gdje ne dozvoljava promjene.");
			
			positive = !positive;
			
		}

		@Override
		public void insertDecimalPoint() throws CalculatorInputException {
			
			if (!editable) throw new CalculatorInputException("Kalkulator je u modu gdje ne dozvoljava promjene.");
			
			if (numbers.indexOf(".")!=-1) throw new CalculatorInputException();
			
			if (numbers.length()==0) throw new CalculatorInputException("Decimal dot is only writable after input of a number.");
			
			numbers += ".";
			
		}

		@Override
		public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
			
			if (!editable) {
				throw new CalculatorInputException("Kalkulator u trenutnom stanju ne dozvoljava unos podataka.");
			}
			
			try {
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

		public String toString() {
			
			if (frozen!=null) return positive ? frozen : "-"+frozen;
			
			if (numbers.length()==0) return positive ? "0" : "-0";	
			
			return positive ? numbers : "-"+numbers;
			
		}

		@Override
		public void freezeValue(String value) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean hasFrozenValue() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void setNumbers(String text) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void emptyStack() {
			// TODO Auto-generated method stub
			
		}
		
	}
}
























































