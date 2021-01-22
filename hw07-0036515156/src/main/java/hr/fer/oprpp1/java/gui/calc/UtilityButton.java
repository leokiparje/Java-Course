package hr.fer.oprpp1.java.gui.calc;

import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * Class UtilityButton represents one button with a simpl action.
 * @author leokiparje
 *
 */

public class UtilityButton extends JButton {

	private static final long serialVersionUID = 1L;
	
	private String text;
	
	private ActionListener actionListener;
	
	/*
	 * Basic constructor
	 */
	
	public UtilityButton(String text, ActionListener actionListener) {
		this.text = text;
		this.actionListener = actionListener;
		
		setText(text);
		setFont(getFont().deriveFont(15f));
		addActionListener(actionListener);
		
	}

}