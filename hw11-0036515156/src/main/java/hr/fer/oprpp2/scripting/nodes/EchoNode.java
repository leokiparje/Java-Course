package hr.fer.oprpp2.scripting.nodes;

import hr.fer.oprpp2.scripting.elems.Element;

/**
 * Class EchoNode represents Node with inner collection.
 * @author leokiparje
 *
 */

public class EchoNode extends Node{
	
	/**
	 * inner array of elements ELement
	 */
	
	private Element[] elements;
	
	/**
	 * public contructor which accepts one argument array of Elements
	 * @param e array of Elements
	 */
	
	public EchoNode(Element[] e) {
		if (e==null) throw new NullPointerException();
		elements = e;
	}

	/**
	 * returns inner collection
	 * @return inner collection
	 */
	
	public Element[] getElements() {
		
		return elements;
		
	}
	
	@Override
	public int numberOfChildren() {
		return 0;
	}

	@Override
	public Node getChild(int index) {
		throw new RuntimeException("EchoNode nema djecu!");
	}

	@Override
	public void addChildNode(Node child) {
		throw new RuntimeException("Nemoguce je dodavati djecu u echo node!");
	}
	
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
        sb.append("{$= ");

        Element[] echoNodeElements = getElements();

        for (Element echoNodeElement : echoNodeElements) {
            sb.append(echoNodeElement.asText());
            sb.append(" ");
        }

        sb.append("$}");
        return sb.toString();
		
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}
	
}
