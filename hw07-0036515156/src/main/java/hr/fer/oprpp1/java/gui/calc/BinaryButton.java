package hr.fer.oprpp1.java.gui.calc;

import java.awt.event.ActionListener;
import java.util.function.BinaryOperator;

import javax.swing.JButton;

/**
 * Class BinaryButton represents one button with a simpl action.
 * @author leokiparje
 *
 */

public class BinaryButton extends JButton {
	
	private static final long serialVersionUID = 1L;

	private String text;
	
	private ActionListener actionListener;
	
	/*
	 * Basic constructor
	 */
	
	public BinaryButton(String text, ActionListener actionListener) {
		this.text = text;
		this.actionListener = actionListener;
		
		setText(text);
		setFont(getFont().deriveFont(15f));
		addActionListener(actionListener);
		
	}
	
}
