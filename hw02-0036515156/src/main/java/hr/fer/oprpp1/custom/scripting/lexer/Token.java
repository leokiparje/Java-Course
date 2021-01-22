package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * Class Token represents one token
 * @author leokiparje
 *
 */

public class Token {
	
	/**
	 * Object value
	 */
	
	Object value;
	
	/**
	 * TokenType type
	 */
	
	TokenType type;

	/**
	 * public contructor that accepts arguments type and value
	 * @param type TokenType type
	 * @param value Object value
	 */
	
	public Token(TokenType type, Object value) {
		this.value=value;
		this.type=type;
	}
	/**
	 * getter for value
	 * @return value
	 */
	
	public Object getValue() {
		return value;
	}
	
	/**
	 * getter for type
	 * @return type
	 */
	
	public TokenType getType() {
		return type;
	}
	
}
