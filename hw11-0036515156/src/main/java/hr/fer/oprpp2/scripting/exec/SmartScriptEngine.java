package hr.fer.oprpp2.scripting.exec;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import hr.fer.oprpp2.scripting.elems.Element;
import hr.fer.oprpp2.scripting.elems.ElementConstantDouble;
import hr.fer.oprpp2.scripting.elems.ElementConstantInteger;
import hr.fer.oprpp2.scripting.elems.ElementFunction;
import hr.fer.oprpp2.scripting.elems.ElementOperator;
import hr.fer.oprpp2.scripting.elems.ElementString;
import hr.fer.oprpp2.scripting.elems.ElementVariable;
import hr.fer.oprpp2.scripting.nodes.DocumentNode;
import hr.fer.oprpp2.scripting.nodes.EchoNode;
import hr.fer.oprpp2.scripting.nodes.ForLoopNode;
import hr.fer.oprpp2.scripting.nodes.INodeVisitor;
import hr.fer.oprpp2.scripting.nodes.Node;
import hr.fer.oprpp2.scripting.nodes.TextNode;
import hr.fer.oprpp2.scripting.parser.SmartScriptParser;
import hr.fer.oprpp2.webserver.RequestContext;
import hr.fer.oprpp2.webserver.RequestContext.RCCookie;

public class SmartScriptEngine {

	private DocumentNode documentNode;
	private RequestContext requestContext;
	private ObjectMultistack multistack = new ObjectMultistack();
	
	private INodeVisitor visitor = new INodeVisitor() {

		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.toString());
			} catch (IOException e) {
				System.out.println("Unable to write text node to request context.");
				e.printStackTrace();
			}
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			String variable = node.getVariable().asText();
			ValueWrapper startExpression = new ValueWrapper(node.getStartExpression().asText());
			ValueWrapper endExpression = new ValueWrapper(node.getEndExpression().asText());
			ValueWrapper stepExpression = new ValueWrapper(node.getStepExpression().asText());
			
			multistack.push(variable, startExpression);
			
			while(multistack.peek(variable).numCompare(endExpression.getValue())<=0) {
				for (int i=0;i<node.numberOfChildren();i++) {
					node.getChild(i).accept(this);
				}
				multistack.peek(variable).add(stepExpression.getValue());
			}
			multistack.pop(variable);
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Stack<ValueWrapper> temporaryStack = new Stack<>();
			for (Element el : node.getElements()) {
				if (isConstant(el)) {
					pushToStack(temporaryStack,el);
				}else {
					if (el instanceof ElementFunction) {
						switch(el.asText()) {
						case "@sin":
							ValueWrapper x = temporaryStack.pop();
							double sinNum = x.getDoubleValue();
							double angle = sinNum*Math.PI/180;
							ValueWrapper newValue = new ValueWrapper(angle);
							temporaryStack.push(newValue);
							break;
						case "@decfmt":
							DecimalFormat format = new DecimalFormat(temporaryStack.pop().getValue().toString());
							double num = temporaryStack.pop().getDoubleValue();
							temporaryStack.push(new ValueWrapper(format.format(num)));
							break;
						case "@dup":
							ValueWrapper duplicate = temporaryStack.pop();
							temporaryStack.push(duplicate);
							temporaryStack.push(duplicate);
							break;
						case "@swap":
							ValueWrapper first = temporaryStack.pop();
							ValueWrapper second = temporaryStack.pop();
							temporaryStack.push(first);
							temporaryStack.push(second);
							break;
						case "@setMimeType":
							ValueWrapper mimeType = temporaryStack.pop();
							requestContext.setMimeType(mimeType.getValue().toString());
							break;
						case "@paramGet":
							ValueWrapper dv = temporaryStack.pop();
                            ValueWrapper name = temporaryStack.pop();
                            String value = requestContext.getParameter(name.getValue().toString());
                            temporaryStack.push(new ValueWrapper(value==null ? dv.getValue().toString() : value));
                            break;
						case "@pparamGet":
							ValueWrapper dv2 = temporaryStack.pop();
                            ValueWrapper name2 = temporaryStack.pop();
                            String value2 = requestContext.getPersistentParameter(name2.getValue().toString());
                            temporaryStack.push(new ValueWrapper(value2==null ? dv2.getValue().toString() : value2));
                            break;
						case "@pparamSet":
							ValueWrapper setName = temporaryStack.pop();
                            ValueWrapper setValue = temporaryStack.pop();
                            requestContext.setPersistentParameter(setName.getValue().toString(),setValue.getValue().toString());
                            break;
						case "@pparamDel":
							ValueWrapper delName = temporaryStack.pop();
                            requestContext.removePersistentParameter(delName.toString());
                            break;
						case "@tparamGet":
							ValueWrapper getDv = temporaryStack.pop();
                            ValueWrapper getName = temporaryStack.pop();
                            String getValue = requestContext.getTemporaryParameter(getName.getValue().toString());
                            temporaryStack.push(new ValueWrapper(getValue == null ? getDv.getValue().toString() : getValue));
                            break;
						case "@tparamSet":
							ValueWrapper tName = temporaryStack.pop();
                            ValueWrapper tValue = temporaryStack.pop();
                            requestContext.setTemporaryParameter(tName.getValue().toString(), tValue.getValue().toString());
                            break;
						case "@tparamDel":
							ValueWrapper tparamDelName = temporaryStack.pop();
                            requestContext.removeTemporaryParameter(tparamDelName.toString());
                            break;
                        default:
                            throw new RuntimeException("Fatal error. Impossible state.");
						}
					}else if (el instanceof ElementOperator) {
						ValueWrapper num1 = temporaryStack.pop(); 
						ValueWrapper num2 = temporaryStack.pop(); // u num2 se spremi ValueWrapper
						switch(el.asText()) {
						case "+":
							num1.add(num2.getValue());
							temporaryStack.push(num1);
							break;
						case "-":
							num1.subtract(num2.getValue());
							temporaryStack.push(num1);
							break;
						case "*":
							num1.multiply(num2.getValue());
							temporaryStack.push(num1);
							break;
						case "/":
							num1.divide(num2.getValue());
							temporaryStack.push(num1);
							break;
						}
					}else throw new RuntimeException("Fatal error. Impossible state.");
				}
			}
			
			Iterator<ValueWrapper> iterator = temporaryStack.iterator();
			while(iterator.hasNext()) {
				try {
					requestContext.write(String.valueOf(iterator.next().getValue()));
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i=0;i<node.numberOfChildren();i++) {
				Node child = node.getChild(i);
				child.accept(this);
			}
		}
	
	};
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}
	public void execute() {
		documentNode.accept(visitor);
	}
	
	private boolean isConstant(Element e) {
		if (e instanceof ElementVariable || e instanceof ElementString || e instanceof ElementConstantInteger || e instanceof ElementConstantDouble) return true;
		return false;
	}
	
	private void pushToStack(Stack<ValueWrapper> temporaryStack, Element el) {
		if (el instanceof ElementConstantDouble) {
			temporaryStack.push(new ValueWrapper(((ElementConstantDouble)el).getValue()));
		}else if(el instanceof ElementConstantInteger) {
			temporaryStack.push(new ValueWrapper(((ElementConstantInteger)el).getValue()));
		}else if(el instanceof ElementString) {
			String string = el.asText();
			if (string.startsWith("\"")) string = string.substring(1);
			if (string.endsWith("\"")) string = string.substring(0,string.length()-1);
			temporaryStack.push(new ValueWrapper(string));
		}else if(el instanceof ElementVariable) {
			temporaryStack.push(new ValueWrapper(multistack.peek(el.asText()).getValue()));
		}else throw new RuntimeException("Fatal error. Impossible state.");
	}
	
	public static void main(String[] args) {
		String documentBody = readFromDisk("webroot/scripts/brojPoziva.smscr");
		Map<String,String> parameters = new HashMap<String, String>();
		Map<String,String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		persistentParameters.put("brojPoziva", "3");
		RequestContext rc = new RequestContext(System.out, parameters, persistentParameters,
		cookies);
		new SmartScriptEngine(
		new SmartScriptParser(documentBody).getDocumentNode(), rc
		).execute();
		System.out.println("Vrijednost u mapi: "+rc.getPersistentParameter("brojPoziva"));
	}
	
	private static String readFromDisk(String path) {
		File file = null;
		try {
			file = new File(path);
		}catch(Exception e) { System.out.println("Couldn't open file for given filename."); }
		
		String docBody = null;
		try {
			docBody = Files.readString(file.toPath());
		} catch (IOException e) {
			System.out.println("Unable to read from file.");
			e.printStackTrace();
		}
		return docBody;
	}
	
}
