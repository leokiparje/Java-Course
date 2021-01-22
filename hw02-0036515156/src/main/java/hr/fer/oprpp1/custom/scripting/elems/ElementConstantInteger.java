package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Class ElementConstantInteger represents Element with value int.
 * @author leokiparje
 *
 */

public class ElementConstantInteger extends Element{
	
	/**
	 * Value int
	 */
	
	private int value;
	
	/**
	 * consturctor with one double parametar which is to be set to value
	 * @param value int value
	 */
	
	public ElementConstantInteger(int value) {
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
	
	public int getValue() {
		
		return value;
		
	}
}
