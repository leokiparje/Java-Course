package hr.fer.oprpp2.scripting.nodes;

/**
 * Abstract class Node represents Node object
 * @author leokiparje
 *
 */

public abstract class Node {
	
	/**
	 * adds childNode to current Node
	 * @param child Node
	 */
	
	public abstract void addChildNode(Node child);
	
	/**
	 * returns number of children
	 * @return number of children
	 */
	
	public abstract int numberOfChildren();
	
	/**
	 * return Child at the given index
	 * @param index index
	 * @return Child at the given index
	 */
	
	public abstract Node getChild(int index);
	
	public abstract void accept(INodeVisitor visitor);
}
