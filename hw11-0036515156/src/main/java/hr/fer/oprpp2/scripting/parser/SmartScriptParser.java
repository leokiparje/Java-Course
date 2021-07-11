package hr.fer.oprpp2.scripting.parser;

import java.util.Arrays;

import hr.fer.oprpp2.collections.ArrayIndexedCollection;
import hr.fer.oprpp2.scripting.elems.Element;
import hr.fer.oprpp2.scripting.elems.ElementConstantDouble;
import hr.fer.oprpp2.scripting.elems.ElementConstantInteger;
import hr.fer.oprpp2.scripting.elems.ElementFunction;
import hr.fer.oprpp2.scripting.elems.ElementOperator;
import hr.fer.oprpp2.scripting.elems.ElementString;
import hr.fer.oprpp2.scripting.elems.ElementVariable;
import hr.fer.oprpp2.scripting.lexer.LexerState;
import hr.fer.oprpp2.scripting.lexer.ParsingLexer;
import hr.fer.oprpp2.scripting.lexer.Token;
import hr.fer.oprpp2.scripting.lexer.TokenType;
import hr.fer.oprpp2.scripting.nodes.DocumentNode;
import hr.fer.oprpp2.scripting.nodes.EchoNode;
import hr.fer.oprpp2.scripting.nodes.ForLoopNode;
import hr.fer.oprpp2.scripting.nodes.Node;
import hr.fer.oprpp2.scripting.nodes.TextNode;

/**
 * This class represents Parser. It is used for parsing Tokens into Elements following parsers rules.
 * @author leokiparje
 *
 */

public class SmartScriptParser {
	
	/**
	 * inner stack representation
	 */
	
	private ObjectStack stack;
	
	/**
	 * inner lexer representation
	 */
	
	private ParsingLexer lexer;
	
	/**
	 * inner document DocumentNode
	 */
	
	private DocumentNode document;
	
	/**
	 * basic constructor
	 * @param textFile String
	 */
	
	public SmartScriptParser(String textFile) {
		lexer = new ParsingLexer(textFile);
		stack = new ObjectStack();
		document = new DocumentNode();
		stack.push(document);
		
		lexer.setState(LexerState.TEXT);
		
		try {
			parse();
		}catch(Exception ex) {
			throw new SmartScriptParserException(ex.getMessage());
		}
		
		
	}
	
	/**
	 * getter for document
	 * @return
	 */
	
	public DocumentNode getDocumentNode() {
		return document;
	}
	
	/**
	 * method parses given Tokens and outputs Elements
	 */
	
	public void parse() {
		
		Token trenutni = lexer.nextToken();
		
		while(true) {
			
			if (trenutni.getType()==TokenType.EOF) break;
			
			else if (lexer.getState()==LexerState.TEXT) {
				
				if (trenutni.getType()==TokenType.TEXT) {
					
					TextNode novi = new TextNode(trenutni.getValue().toString());
					Node roditelj = (Node) stack.peek();
					roditelj.addChildNode(novi);
					
					trenutni = lexer.nextToken();
					
				}else break;
				
				
			}else if (trenutni.getType()==TokenType.BEGINTAG) {
				
				trenutni = lexer.nextToken();
				
			}else if (trenutni.getType()==TokenType.EQUAL) {
					
				ArrayIndexedCollection elements = new ArrayIndexedCollection();
				
				while(true) {
					
					trenutni = lexer.nextToken();
					
					if (trenutni.getType()==TokenType.ENDTAG) {
						
						if (elements.size()==0) throw new SmartScriptParserException();
						//lexer.setState(LexerState.TEXT);
						break;
						
					}
					
					elements.add(parseEle(trenutni));
					
				}
				
				Element[] echoElements = Arrays.copyOf(elements.toArray(), elements.size(), Element[].class);
				
				EchoNode eNode = new EchoNode(echoElements);
				Node roditelj = (Node) stack.peek();
				roditelj.addChildNode(eNode);
				
				trenutni = lexer.nextToken();
				
			}else if(trenutni.getType()==TokenType.TEXT) {
				
				if (trenutni.getValue().toString().toUpperCase().equals("FOR")) {
					
					trenutni = lexer.nextToken();
					
					if (!Character.isLetter(trenutni.getValue().toString().charAt(0))) throw new SmartScriptParserException("Kriva forma for petlje!");

					Element[] floopElements = new Element[3]; 
					ElementVariable ev = new ElementVariable(trenutni.getValue().toString());
					
					for (int i=0;i<4;i++) {
						
						trenutni = lexer.nextToken();
						
						if (trenutni.getType()==TokenType.ENDTAG) {
							
							if (i<2) throw new SmartScriptParserException("For tag kirvo zadan!");
							trenutni = lexer.nextToken();
							
							break;
							
						}else if(i==3 && trenutni.getType()!=TokenType.ENDTAG) throw new SmartScriptParserException("For tag krivo zadan!");
						
						floopElements[i] = parseEle(trenutni);
						
					}
					
					ForLoopNode floopNode = new ForLoopNode(ev,floopElements[0],floopElements[1],floopElements[2]);
					Node roditelj = (Node) stack.peek();
					roditelj.addChildNode(floopNode);
					stack.push(floopNode);
					
				}
				else if(trenutni.getValue().toString().equalsIgnoreCase("end")) {
					
					trenutni = lexer.nextToken();
                    if (trenutni.getType()==TokenType.ENDTAG) {
                        //lexer.setState(LexerState.TEXT);
                        stack.pop();
                        trenutni = lexer.nextToken();
                    } 
                    else throw new SmartScriptParserException("Nemoguc Tag");
					
				}
				
			}	
			
		}
		
		if (stack.size()!=1) throw new SmartScriptParserException();
		
	}
	
	/**
	 * parses Token into Element
	 * @param t Token
	 * @return Element
	 */
	
	public Element parseEle(Token t) {
		
		Token trenutni = lexer.getToken();
		
		switch(t.getType()) {
		
			case INTEGER:
				return new ElementConstantInteger((int) trenutni.getValue());
			case DOUBLE:
				return new ElementConstantDouble((double) trenutni.getValue());
			case OPERATOR:
				return new ElementOperator(trenutni.getValue().toString());
			case FUNCTION:
				return new ElementFunction(trenutni.getValue().toString());
			case TEXT:
				if (trenutni.getValue().toString().charAt(0)=='\"') return new ElementString(trenutni.getValue().toString());
				else {
					
					if (!Character.isLetter(trenutni.getValue().toString().charAt(0))) throw new SmartScriptParserException("Krivo zadana varijabla!");
					return new ElementVariable(trenutni.getValue().toString());
				}
			default:
				throw new SmartScriptParserException();
		}
		
		
	}
	
}
