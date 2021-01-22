package hr.fer.oprpp1.hw02.prob1;

/**
 * This class represents Lexer. It is used for parsing text into Tokens following lexer rules.
 * @author leokiparje
 *
 */

public class Lexer {
	
	/**
	 * inner collection of characters
	 */
	
	private char[] data;
	
	/**
	 * last generated token
	 */
	
	private Token token;
	
	/**
	 * current LexerState
	 */
	
	private LexerState state;
	
	/**
	 * current index
	 */
	
	private int currentIndex; 
	
	/**
	 * boolean flag
	 */
	
	private boolean flag = false;
	
	/**
	 * basic constructor
	 * @param text String text
	 */
	
	public Lexer(String text) { 
		data = text.toCharArray();
		currentIndex=0;
		state = LexerState.BASIC;
	}
	
	/**
	 * retunrs new Token
	 * @return new Token
	 */
	
	public Token nextToken() { 
		if (state==LexerState.BASIC) return parse();
		return parseExtended();
	}
	
	/**
	 * retuns Token
	 * @return Token
	 */
	
	public Token getToken() {
		return token;
	}
	
	/**
	 * parses in extended lexerState
	 * @return
	 */
	
	public Token parseExtended() {
		
		if (token!=null && token.getType() == TokenType.EOF) throw new LexerException("Dohvat novog tokena nakon EOF tokena nije moguc!");
		
		skip();
		
		TokenType type = TokenType.EOF;
		StringBuilder sb = new StringBuilder();
		
		while(currentIndex<data.length) {
			
			if (data[currentIndex]=='#') {
				
				if (type==TokenType.EOF) {
					token = new Token(TokenType.SYMBOL,data[currentIndex++]);
					setState(LexerState.BASIC);
					break;
				}
				token = new Token(type, sb.toString());
				break;
				
				
			}
			
			if (data[currentIndex]==' ' || data[currentIndex]=='\n' || data[currentIndex]=='\t' || data[currentIndex]=='\r') {
				token = new Token(TokenType.WORD, sb.toString());
				break;
			}
			type=TokenType.WORD;
			sb.append(data[currentIndex++]);
			
		}
		
		
		if (currentIndex==data.length && type==TokenType.EOF) token = new Token(TokenType.EOF,null);
		else if (currentIndex==data.length) token = new Token(TokenType.WORD, sb.toString());
		
		return token;
		
	}
	
	/**
	 * parses in regular lexerState
	 * @return
	 */
	
	public Token parse() {
		
		if (token!=null && token.getType() == TokenType.EOF) throw new LexerException("Dohvat novog tokena nakon EOF tokena nije moguc!");
		
		skip();
		
		TokenType type = TokenType.EOF;
		StringBuilder sb = new StringBuilder();
		
		while(currentIndex<data.length) {
			
			if (Character.isLetter(data[currentIndex])) {
				
				if (flag) throw new LexerException();
				
				if (type==TokenType.EOF) type = TokenType.WORD;
				
				else if (type!=TokenType.WORD) break;
				
			}else if (Character.isDigit(data[currentIndex])){
				
				if (flag) {
					type = TokenType.WORD;
					flag=false;
				}
				
				else if (type==TokenType.EOF) type = TokenType.NUMBER;
				
				else if (type!=TokenType.NUMBER) break;
				
			}else if(data[currentIndex]=='\\') {
				
				if (type==TokenType.SYMBOL) break;
				
				if (!flag) {
					type =TokenType.WORD;
					currentIndex++;
					flag=true;
					continue;
				}
				flag=false;
				
			}else if (checkSymbol()) {
				
				if (flag) throw new LexerException();
				
				if (type==TokenType.EOF) type = TokenType.SYMBOL;
				
				else break;
				
			}else {
				if (flag) throw new LexerException();
				
				if(type==TokenType.NUMBER) {
					
					try {
						token = new Token(type,Long.parseLong(sb.toString()));
					}catch(NumberFormatException ex) {
						throw new LexerException("ulaz ne valja!");
					}	
					
				}else if (type==TokenType.SYMBOL) {
					
					token = new Token(type,sb.toString().charAt(0));
					if (token.getValue().equals('#')) setState(LexerState.EXTENDED);
					
				}else {
					token = new Token(type,sb.toString());
				}
				return token;
			}
			
			sb.append(data[currentIndex++]);
			
		}
		
		if (currentIndex==data.length) {
			if (flag) throw new LexerException();
			if (type==TokenType.EOF) token = new Token(TokenType.EOF, null);
		}
		
		switch (type) {
			case WORD:
				token = new Token(TokenType.WORD, sb.toString());
				break;
			case NUMBER:
				try {
					token = new Token(type,Long.parseLong(sb.toString()));
				}catch(NumberFormatException ex) {
					throw new LexerException("ulaz ne valja!");
				}
				break;
			case SYMBOL:
				token = new Token(type,sb.toString().charAt(0));
				break;
		}
		
		if (token.getType()==TokenType.SYMBOL && token.getValue().equals('#')) setState(LexerState.EXTENDED);
		
		return token;
		
	}
	
	/**
	 * returns boolean value if character is equal to symbol
	 * @return boolean true or false
	 */
	
	public boolean checkSymbol() {
		if (data[currentIndex] == ' ' || data[currentIndex] == '\t' || data[currentIndex] == '\r' || data[currentIndex] == '\n') {
			return false;
		}
		return true;
	}
	
	/**
	 * skips all the whitespaces till next non whitespace character
	 */
	
	public void skip() {
		for (int i=currentIndex;i<data.length;i++) {
			if (data[i]==' ' || data[i]=='\n' || data[i]=='\t' || data[i]=='\r') continue;
			currentIndex = i;
			return;
		}
		currentIndex = data.length;
	}
	
	/**
	 * sets State
	 * @param state LexerState
	 */
	
	public void setState(LexerState state) {
		
		if (state==null) throw new NullPointerException();
		this.state = state;
		
	}
	
}
