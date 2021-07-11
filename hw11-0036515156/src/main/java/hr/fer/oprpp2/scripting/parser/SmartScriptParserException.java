package hr.fer.oprpp2.scripting.parser;

/**
* class SmartScriptParserException extends RuntimeException
* @author leokiparje
*
*/

public class SmartScriptParserException extends RuntimeException{
	
	/**
	 * default constructor
	 */
	
	public SmartScriptParserException() {}
	
	/**
	 * constructor with argument String message
	 * @param message
	 */
	
	public SmartScriptParserException(String message) {
		super(message);
	}

}
