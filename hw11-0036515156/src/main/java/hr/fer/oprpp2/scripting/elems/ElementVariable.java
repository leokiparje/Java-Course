package hr.fer.oprpp2.scripting.elems;

/**
 * Class ElementVariable represents Element with value string.
 * @author leokiparje
 *
 */

public class ElementVariable extends Element{
	
	/**
	 * String name
	 */
	
	private String name;
	
	/**
	 * public constructor with argument String name
	 * @param name String name
	 */
	
	public ElementVariable(String name) {
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
