package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Class ElementFunction represents Element with value string.
 * @author leokiparje
 *
 */

public class ElementFunction extends Element{

	/**
	 * name string
	 */
	
	private String name;
	
	/**
	 * constructor with String argument
	 * @param name String argument
	 */
	
	public ElementFunction(String name) {
		if (name==null) throw new NullPointerException();
		this.name=name;
	}
	
	/**
	 * returns String representation of Element
	 */
	
	public String asText() {
		return name;
	}
	
}
