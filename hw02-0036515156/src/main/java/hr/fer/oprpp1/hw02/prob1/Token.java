package hr.fer.oprpp1.hw02.prob1;

/**
 * class represents Token
 * @author leokiparje
 *
 */

public class Token {
	
	Object value;
	TokenType type;

	public Token(TokenType type, Object value) {
		this.value=value;
		this.type=type;
	}
	public Object getValue() {
		return value;
	}
	public TokenType getType() {
		return type;
	}
	
}
