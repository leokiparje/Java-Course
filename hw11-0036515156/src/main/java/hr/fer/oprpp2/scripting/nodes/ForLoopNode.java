package hr.fer.oprpp2.scripting.nodes;

import hr.fer.oprpp2.collections.ArrayIndexedCollection;
import hr.fer.oprpp2.scripting.elems.Element;
import hr.fer.oprpp2.scripting.elems.ElementVariable;

/**
 * Class ForLoopNode represents Node with inner collection.
 * @author leokiparje
 *
 */

public class ForLoopNode extends Node{
	
	/**
	 * array inner collection
	 */
	
	private ArrayIndexedCollection array;
	
	/**
	 * variable ELementVariable
	 */
	
	private ElementVariable variable;
	
	/**
	 * Element startExpression
	 */
	
	private Element startExpression;
	
	/**
	 * Element endExpression
	 */
	
	private Element endExpression;
	
	/**
	 * Element startExpression
	 */
	
	private Element stepExpression;
	
	/**
	 * public constructor
	 * @param variable ELementVariable
	 * @param startExpression Element
	 * @param endExpression Element
	 * @param stepExpression Element
	 */
	
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression) {
		this.variable=variable;
		this.startExpression=startExpression;
		this.endExpression=endExpression;
		this.stepExpression=stepExpression;
	}

	@Override
	public int numberOfChildren() {
		return array.size();
	}

	@Override
	public Node getChild(int index) {
		return (Node) array.get(index);
	}
	
	/**
	 * getter for variable
	 * @return variable
	 */

	
	public ElementVariable getVariable() {
		
		return variable;
		
	}
	
	/**
	 * getter for startExpression
	 * @return startExpression
	 */
	
	public Element getStartExpression() {
		
		return startExpression;
		
	}
	
	/**
	 * getter for startExpression
	 * @return startExpression
	 */
	
	public Element getEndExpression() {
		
		return endExpression;
		
	}
	
	/**
	 * getter for stepExpression
	 * @return stepExpression
	 */
	
	public Element getStepExpression() {
		
		return stepExpression;
		
	}

	@Override
	public void addChildNode(Node child) {
		if (child==null) throw new NullPointerException();
		if (array==null) array = new ArrayIndexedCollection();
		
		array.add(child);
		
	}
	
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
        sb.append("{$ FOR ");
        sb.append(getVariable().asText());
        sb.append(" ");
        sb.append(getStartExpression().asText());
        sb.append(" ");
        sb.append(getEndExpression().asText());
        sb.append(" ");
        if (getStepExpression() != null) {
            sb.append(getStepExpression().asText());
            sb.append(" ");
        }
        sb.append("$}");
        
        int i = 0;
        
        while (i < numberOfChildren()) {
            Node child = getChild(i);

            if (child instanceof TextNode) {
                sb.append(((TextNode) child).getText());
            } else if (child instanceof EchoNode) {
                sb.append(child.toString());
            } else
                System.out.println("Neispravno stablo!");
            i++;
        }

        sb.append("{$ END $}");

        return sb.toString();
		
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitForLoopNode(this);
	}
	
}
