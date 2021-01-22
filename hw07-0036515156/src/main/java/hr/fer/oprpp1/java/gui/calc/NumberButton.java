package hr.fer.oprpp1.java.gui.calc;

import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Class NumberButton represents a number ranging from 0 to 9 on calculator
 * @author leokiparje
 *
 */

public class NumberButton extends JButton {

	private static final long serialVersionUID = 1L;
	
	private String text;
	
	private ActionListener actionListener;
	
	/*
	 * Basic constructor
	 */
	
	public NumberButton(String text, ActionListener actionListener) {
		this.text = text;
		this.actionListener = actionListener;
		
		setText(text);
		setFont(getFont().deriveFont(30f));
		addActionListener(actionListener);
		
	}

}
