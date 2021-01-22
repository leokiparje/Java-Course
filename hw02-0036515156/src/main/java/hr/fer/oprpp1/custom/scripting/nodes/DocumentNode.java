package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;

/**
 * Class DocumentNode represents Node with inner collection.
 * @author leokiparje
 *
 */

public class DocumentNode extends Node{
	
	/**
	 * inner collection ArrayIndexedCollection
	 */
	
	private ArrayIndexedCollection array;
	
	@Override
	public int numberOfChildren() {
		return array.size();
	}

	@Override
	public Node getChild(int index) {
		if (index<0 || index>=array.size()) throw new IndexOutOfBoundsException();
		return (Node) array.get(index);
	}

	@Override
	public void addChildNode(Node child) {
		if (child==null) throw new NullPointerException();
		if (array==null) array = new ArrayIndexedCollection();
		array.add(child);
		
	}
	
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		for (int i=0;i<numberOfChildren();i++) {
			
			Node node = (Node) getChild(i);
			sb.append(node.toString());
			
		}
		
		return sb.toString();
		
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof DocumentNode) || other==null) return false;
		DocumentNode novi = (DocumentNode) other;
		return novi.toString().equals(this.toString());
	}

}























