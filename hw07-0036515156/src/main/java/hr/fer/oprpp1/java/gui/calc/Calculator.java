package hr.fer.oprpp1.java.gui.calc;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.oprpp1.java.gui.calc.model.CalcModel;
import hr.fer.oprpp1.java.gui.calc.model.CalcModelImpl;
import hr.fer.oprpp1.java.gui.layouts.CalcLayout;

/**
 * Class Calculator represents a calculator with basic calculator functionality.
 * @author leokiparje
 *
 */

public class Calculator extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private CalcModel model = new CalcModelImpl();
	
	private Stack<Double> stack = new Stack<>();
	
	private Set<UnaryButton> inversable = new HashSet<>();

	public Calculator() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		initGUI();
		pack();
	}
	
	private void initGUI() {
		
		Container cp = getContentPane();
		cp.setLayout(new CalcLayout(4));
		
		JLabel calc = new JLabel(model.toString());
		calc.setOpaque(true);
		calc.setBackground(Color.YELLOW);
		calc.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		calc.setHorizontalAlignment(SwingConstants.RIGHT);
		calc.setFont(new Font("Verdana", Font.BOLD, 30));
		
		model.addCalcValueListener(model -> calc.setText(model.toString()));
		
		cp.add(calc, "1,1");
		
		cp.add(new NumberButton("0", n -> model.insertDigit(0)), "5,3");
		cp.add(new NumberButton("1", n -> model.insertDigit(1)), "4,3");
		cp.add(new NumberButton("2", n -> model.insertDigit(2)), "4,4");
		cp.add(new NumberButton("3", n -> model.insertDigit(3)), "4,5");
		cp.add(new NumberButton("4", n -> model.insertDigit(4)), "3,3");
		cp.add(new NumberButton("5", n -> model.insertDigit(5)), "3,4");
		cp.add(new NumberButton("6", n -> model.insertDigit(6)), "3,5");
		cp.add(new NumberButton("7", n -> model.insertDigit(7)), "2,3");
		cp.add(new NumberButton("8", n -> model.insertDigit(8)), "2,4");
		cp.add(new NumberButton("9", n -> model.insertDigit(9)), "2,5");
		
		cp.add(new UtilityButton("+/-", n -> model.swapSign()), "5,4");
		cp.add(new UtilityButton(".", n -> model.insertDecimalPoint()), "5,5");		
		cp.add(new UtilityButton("clr", n -> model.clear()), "1,7");
		cp.add(new UtilityButton("res", n -> model.clearAll()), "2,7");
		cp.add(new UtilityButton("push", n -> stack.push(model.getValue())), "3,7");
		cp.add(new UtilityButton("pop", n -> {
			if (stack.isEmpty()) {
				model.emptyStack();
			}else {
				model.setValue(stack.pop());
			}
		}), "4,7");
		
		JCheckBox inv = new JCheckBox("Inv");
		inv.addItemListener(l -> {
			for (UnaryButton u : inversable) {
				u.inverse();
			}
		});
		cp.add(inv, "5,7");
		
		cp.add(new BinaryButton("+", p -> {
			binaryOperation();
			model.setPendingBinaryOperation((first,second) -> first + second);
		}), "5,6");
		
		cp.add(new BinaryButton("-", p -> {
			binaryOperation();
			model.setPendingBinaryOperation((first,second) -> first - second);
		}), "4,6");
		
		cp.add(new BinaryButton("*", p -> {
			binaryOperation();
			model.setPendingBinaryOperation((first,second) -> first * second);
		}), "3,6");
		
		cp.add(new BinaryButton("/", p -> {
			binaryOperation();
			model.setPendingBinaryOperation((first,second) -> first / second);
		}), "2,6");
		
		cp.add(new UtilityButton("=", e -> {
			if (model.isActiveOperandSet() && model.getPendingBinaryOperation()!=null) {
				model.setValue(model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue()));
				model.setPendingBinaryOperation(null);
			}
		}), "1,6");
		
		cp.add(new UtilityButton("1/x", r -> model.setValue(1/model.getValue())), "2,1");
		
		UnaryButton sinButton = new UnaryButton("sin", "arcsin", u -> model.setValue(Math.sin(model.getValue()))
				, u -> model.setValue(Math.asin(model.getValue())));
		
		cp.add(sinButton, "2,2");
		
		UnaryButton cosButton = new UnaryButton("cos", "arccos", u -> model.setValue(Math.cos(model.getValue()))
				, u -> model.setValue(Math.acos(model.getValue())));
		
		cp.add(cosButton, "3,2");
		
		UnaryButton tanButton = new UnaryButton("tan", "arctan", u -> model.setValue(Math.tan(model.getValue()))
				, u -> model.setValue(Math.atan(model.getValue())));
		
		cp.add(tanButton, "4,2");
		
		UnaryButton ctgButton = new UnaryButton("ctg", "arcctg", u -> model.setValue(1/Math.tan(model.getValue()))
				, u -> model.setValue(Math.PI/2 - Math.asin(model.getValue())));
		
		cp.add(ctgButton, "5,2");
		
		UnaryButton logButton = new UnaryButton("log", "10^x", u -> model.setValue(Math.log10(model.getValue()))
				, u -> model.setValue(Math.pow(10, model.getValue())));
		
		cp.add(logButton , "3,1");
		
		UnaryButton lnButton = new UnaryButton("ln", "e^x", u -> model.setValue(Math.log(model.getValue()))
				, u -> model.setValue(Math.pow(Math.E, model.getValue())));
		
		cp.add(lnButton, "4,1");
		
		UnaryButton xnButton = new UnaryButton("x^n", "x^(1/n)", u -> {
			binaryOperation();
			model.setPendingBinaryOperation(Math::pow);
		}, u -> {
			binaryOperation();
			model.setPendingBinaryOperation((first, second) -> Math.pow(first, 1/second));
		});
		
		cp.add(xnButton, "5,1");
		
		inversable.add(sinButton);
		inversable.add(xnButton);
		inversable.add(lnButton);
		inversable.add(logButton);
		inversable.add(ctgButton);
		inversable.add(tanButton);
		inversable.add(cosButton);
		
	}
	
	/*
	 * Method binaryOperation invokes every time binaryButton is pressed to calculate the result of the operation.
	 */
	
	public void binaryOperation() {
		
		if (model.getPendingBinaryOperation()==null) {
			model.setActiveOperand(model.getValue());
			model.freezeValue(model.toString());
			model.clear();
		}
		
		else if (model.isActiveOperandSet()) {
			model.setValue(model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue()));
			model.setActiveOperand(model.getValue());
			model.freezeValue(model.toString());
			model.clear();
		}
	}
	
	/*
	 * Method main
	 */
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			new Calculator().setVisible(true);
		});
	}
	
}






































































