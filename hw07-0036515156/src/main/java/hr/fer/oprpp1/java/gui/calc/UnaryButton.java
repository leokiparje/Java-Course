package hr.fer.oprpp1.java.gui.calc;

import java.awt.event.ActionListener;
import java.util.function.UnaryOperator;

import javax.swing.JButton;

/**
 * Class UnaryButton represents one button with a simpl action and its iverse function.
 * @author leokiparje
 *
 */

public class UnaryButton extends JButton {

	private static final long serialVersionUID = 1L;
	
	private String text;
	
	private String inverse;
	
	private ActionListener actionListener;
	
	private ActionListener inverseListener;
	
	private boolean flag = true;
	
	/*
	 * Basic contructor
	 */
	
	public UnaryButton(String text, String inverse, ActionListener actionListener, ActionListener inverseListener) {
		this.inverse = inverse;
		this.text = text;
		this.actionListener = actionListener;
		this.inverseListener = inverseListener;
		
		setText(text);
		setFont(getFont().deriveFont(15f));
		addActionListener(actionListener);
	}
	
	/*
	 * Inverse method that sets button do its inverse funciton 
	 */
	
	public void inverse() {
		if (flag) {
			setText(inverse);
			removeActionListener(actionListener);
			addActionListener(inverseListener);
			flag = false;
		}else {
			setText(text);
			removeActionListener(inverseListener);
			addActionListener(actionListener);
			flag = true;
		}
	}
}
