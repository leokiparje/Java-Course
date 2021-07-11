package hr.fer.oprpp2.scripting.lexer;

/**
 * This class represents Lexer. It is used for parsing text into Tokens following lexer rules.
 * @author leokiparje
 *
 */

public class ParsingLexer {
	
	/**
	 * inner collection of characters
	 */
	
	private char[] data;
	
	/**
	 * last generated token
	 */
	
	private Token token;
	
	/**
	 * current lexerState
	 */
	
	private LexerState state;
	
	/**
	 * current Index
	 */
	
	private int currentIndex; 
	
	/**
	 * Basic constructor
	 * @param document String document
	 */
	
	public ParsingLexer(String document) {
		if (document==null) throw new NullPointerException();
		data = document.toCharArray();
		state = LexerState.TEXT;
	}
	
	/**
	 * returns nextToken
	 * @return nextToken
	 */
	
	public Token nextToken() { 
		if (state==LexerState.TAG) return parseTag();
		return parseText();
	}
	
	/**
	 * getter for Token
	 * @return Token
	 */
	
	public Token getToken() {
		return token;
	}
	
	/**
	 * parses tag 
	 * @return generated Token
	 */
	
	public Token parseTag() {
		
		if (token!=null && token.getType() == TokenType.EOF) throw new LexerException("Dohvat novog tokena nakon EOF tokena nije moguc!");
		
		skip();
		
		if (data[currentIndex]=='=') {
			
			token = new Token(TokenType.EQUAL, '=');
			currentIndex++;
			return token;
			
		}
		
		TokenType type = TokenType.EOF;
		
		StringBuilder sb = new StringBuilder();
		
		boolean inTagQuotes = false;
		
		while(currentIndex<data.length) {
			
			if (inTagQuotes) {

                if (data[currentIndex] == '\\') {
                	if (++currentIndex==data.length) throw new LexerException();
                    if (data[currentIndex] == '\\' || data[currentIndex] == '"') {
                        sb.append(data[currentIndex++]);
                    } else if (data[currentIndex] == 'n') {
                        sb.append("\n");
                        currentIndex++;
                    } else if (data[currentIndex] == 'r') {
                        sb.append("\r");
                        currentIndex++;
                    } else if (data[currentIndex] == 't') {
                        sb.append("\t");
                        currentIndex++;
                    } else throw new LexerException("Invalid character!");
                    
                } else if (data[currentIndex] == '"') {
                    sb.append(data[currentIndex++]);
                    break;
                } else {
                    sb.append(data[currentIndex++]);
                }

            }else if(data[currentIndex]=='"') {
            	
            	if (type==TokenType.EOF) type = TokenType.TEXT;
            	else if (type!=TokenType.TEXT) break;
            	inTagQuotes = true;
            	sb.append(data[currentIndex++]);
            	
            }else if (data[currentIndex]==' ') {
				
				token = new Token(type, sb.toString());     // opaska jer sve pretvara u string
				break;
				
			}else if(data[currentIndex]=='_') {
				
				if (type==TokenType.EOF) {
					type = TokenType.TEXT;
					sb.append(data[currentIndex++]);
				}else if(type==TokenType.FUNCTION) sb.append(data[currentIndex++]);
				else if(type!=TokenType.TEXT) throw new LexerException();
				else sb.append(data[currentIndex++]);
				
			}else if (Character.isLetter(data[currentIndex])) {
				
				if (type!=TokenType.TEXT && type!=TokenType.FUNCTION) type = TokenType.TEXT;
					
				while (!checkSpaces()) {
					if (data[currentIndex]=='$') break;
					sb.append(data[currentIndex++]);
	            }
				break;
					
				
			}else if(Character.isDigit(data[currentIndex])) {
				
				if (type==TokenType.EOF) type = TokenType.INTEGER;
				else if(type==TokenType.INTEGER || type==TokenType.DOUBLE || type==TokenType.TEXT) sb.append(data[currentIndex++]);
				else if(type==TokenType.FUNCTION) sb.append(data[currentIndex++]);
				else break;
				
			}else if(data[currentIndex]=='-') {
				if (type==TokenType.EOF) {
					type = TokenType.MINUS;
					currentIndex++;
					continue;
				}
				else if (type==TokenType.MINUS) throw new LexerException("Two minuses in a row");
				else break;
			}else if(isOperator()) {
				
				if (type==TokenType.BEGINTAG) throw new LexerException("Invalid character");	
				else if (type==TokenType.EOF) {
					type = TokenType.OPERATOR;
					sb.append(data[currentIndex++]);
				}
				else if (type==TokenType.OPERATOR) throw new LexerException();
				else break;
				
			}else if(data[currentIndex]=='$') {
				
				if (sb.length()>0) {
					token = new Token(type, sb.toString());
					break;
				}
				
				if (data[++currentIndex]!='}') throw new LexerException();
				
				currentIndex++;
				state = LexerState.TEXT;
				return new Token(TokenType.ENDTAG, "$}");
				
			}else if(data[currentIndex]=='@') {				
				
				if (!Character.isLetter(data[currentIndex+1])) throw new LexerException();
				
				type = TokenType.FUNCTION;
				sb.append(data[currentIndex++]);
				
			}else if(checkSymbol() && data[currentIndex]!=' ') {
				
				if (type!=TokenType.TEXT) break;
				sb.append(data[currentIndex++]);
				
			}else break;
			
		}
		
		switch(type) {
			case INTEGER:
				try {
					token = new Token(type,Integer.parseInt(sb.toString()));					
				}catch(Exception e) {
					throw new LexerException();
				}
				break;
			case DOUBLE:
				try {
					token = new Token(type,Double.parseDouble(sb.toString()));
				}catch(Exception e) {
					throw new LexerException();
				}
				break;
			case EOF:
				token = new Token(TokenType.EOF, null);
				break;
			default:
				token = new Token(type, sb.toString());
				break;
		}
		
		return token;
		
	}
			
	/**
	 * parses text
	 * @return generated Token
	 */
	
	public Token parseText() {
		
		if (token!=null && token.getType() == TokenType.EOF) throw new LexerException("Dohvat novog tokena nakon EOF tokena nije moguc!");
		
		StringBuilder sb = new StringBuilder();
		
		while(currentIndex<data.length) {
			
			if (data[currentIndex]=='\\') {
				
				if (currentIndex+1 == data.length) throw new LexerException("Invalid character");
				
				if (data[++currentIndex]=='\\') {
					sb.append('\\');
					sb.append('\\');
					currentIndex++;
					continue;
				}else if(data[currentIndex]=='{') {
					sb.append('\\');
					sb.append('{');
					currentIndex++;
					continue;
				}else {
					throw new LexerException("Invalid character");
				}
				
			}else if(data[currentIndex]=='{') {
				
				if(data[currentIndex+1]=='$') {
					
					if (sb.length()==0) {
						
						currentIndex+=2;
						state = LexerState.TAG;
						return new Token(TokenType.BEGINTAG, "{$");
						
					}
					
					break;
					
				}
				sb.append(data[currentIndex++]);
				
			}else sb.append(data[currentIndex++]);
		
		}
		
		if (sb.length()==0) token = new Token(TokenType.EOF, null);
		else token = new Token(TokenType.TEXT, sb.toString());
		
		return token;
		
			
		
		}
			
			
			/**
			 * returns boolean value if character is equal to operator
			 * @return boolean true or false
			 */
	
	public boolean isOperator() {
		
		if (data[currentIndex] == '+' || data[currentIndex] == '-' || data[currentIndex] == '*' || data[currentIndex] == '/' || data[currentIndex] == '^') return true;
		return false;
		
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
	
	/**
	 * getter for LexerState
	 * @return LexerState
	 */
	
	public LexerState getState() {
		
		return state;
		
	}
	
	/**
	 * returns boolean value if character is equal to whitespace
	 * @return boolean true or false
	 */
	
	private boolean checkSpaces() {
        return data[currentIndex] == '\r' || data[currentIndex] == '\n' || data[currentIndex] == '\t' || data[currentIndex] == ' ';

    }
	
}
	
	


