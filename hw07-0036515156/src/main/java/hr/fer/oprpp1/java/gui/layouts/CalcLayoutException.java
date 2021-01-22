package hr.fer.oprpp1.java.gui.layouts;

/**
 * Class CalcLayoutException represents exception thrown when error occurs in calculator program.
 * @author leokiparje
 *
 */

public class CalcLayoutException extends RuntimeException {

	private static final long serialVersionUID = 4040704682261162770L;
	
	public CalcLayoutException() {
		super();
	}
	
	/*
	 * Basic contructor
	 */
	
	public CalcLayoutException(String message) {
		super(message);
	}
	
}
