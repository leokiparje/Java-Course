package hr.fer.oprpp2.scripting.elems;

/**
 * Class ElementString represents Element with value string.
 * @author leokiparje
 *
 */

public class ElementString extends Element{
	
	/**
	 * value String
	 */
	
	private String value;
	
	/**
	 * constructor with argument String name
	 * @param name String name
	 */
	
	public ElementString(String name) {
		if (name==null) throw new NullPointerException();
		this.value=name;
	}
	
	/**
	 * returns String representation of Element
	 */
	
	public String asText() {
		return value;
	}
	
}
