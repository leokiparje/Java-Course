package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Class ElementConstantDouble represents Element with value double.
 * @author leokiparje
 *
 */

public class ElementConstantDouble extends Element{
	
	/**
	 * Value double
	 */
	
	private double value;
	
	/**
	 * consturctor with one double parametar which is to be set to value
	 * @param value double value
	 */
	
	public ElementConstantDouble(double value) {
		this.value=value;
	}
	
	/**
	 * retunrs String representation of Element
	 */
	
	public String asText() {
		return String.valueOf(value);
	}
	
	/**
	 * returns value
	 * @return value
	 */
	
	public double getValue() {
		
		return value;
		
	}
	
}
