package hr.fer.oprpp1.custom.scripting.nodes;

/**
 * Class TextNode represents Node object
 * @author leokiparje
 *
 */

public class TextNode extends Node{
	
	/**
	 * String text
	 */
	
	private String text;
	
	/**
	 * public constructor
	 * @param text String
	 */
	
	public TextNode(String text) {
		if (text==null) throw new NullPointerException();
		this.text = text;
	}
	
	/**
	 * getter for String text
	 * @return String text
	 */
	
	public String getText() {
		return text;
	}

	@Override
	public int numberOfChildren() {
		return 0;
	}

	@Override
	public Node getChild(int index) {
		throw new RuntimeException("TextNode nema djecu!");
	}

	@Override
	public void addChildNode(Node child) {
		throw new RuntimeException("TextNode nema djecu!");
		
	}
	
	@Override
	public String toString() {
		
		return text;
		
	}
	
	
}
