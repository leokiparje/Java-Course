package hr.fer.oprpp1.hw04.db;

/**
 * Class Lexer implements lexer which from given String retunrs tokens 
 * @author leokiparje
 *
 */

public class Lexer {
	
	private char[] data;

	private Token token;
	
	private int currentIndex;
	
	public Lexer(String string) {
		data = string.toCharArray();
	}
	
	public Token getToken() {
		return token;
	}
	
	public Token nextToken() {
		
		if (token!=null && token.getType() == TokenType.EOF) throw new LexerException("Dohvat novog tokena nakon EOF tokena nije moguc!");
		
		skip();
		
		TokenType type = TokenType.EOF;
		
		StringBuilder sb = new StringBuilder();
		
		boolean inTagQuotes = false;
		
		while(currentIndex<data.length) {
			
			if (inTagQuotes) {
				
				if (data[currentIndex++]=='"') break;
				sb.append(data[currentIndex-1]);
				
			}else if(isWhiteSpace()){
				
				break;
				
			}else if(isOperator()) {
			
				
				if (type==TokenType.EOF) type = TokenType.OPERATOR;
				else break;
				
				sb.append(data[currentIndex++]);
				
				if (currentIndex+1<=data.length) {
					if (data[currentIndex]=='=') {
						sb.append(data[currentIndex++]);
					}
				}
				
			}else if (data[currentIndex]=='"') {
				
				if (type==TokenType.EOF) type = TokenType.STRING;
				else break;
				
				inTagQuotes=true;
				currentIndex++;
			
			}else {
				
				sb.append(data[currentIndex++]);
				type = TokenType.FIELD;
				
			}
			
		}
		
		switch(type) {
			case EOF:
				token = new Token(null,TokenType.EOF);
				break;
			case FIELD:
				String fieldName = sb.toString();
				if (fieldName.toUpperCase().equals("AND")) {
					token = new Token(fieldName, TokenType.AND);
					break;
				}else if(fieldName.toUpperCase().equals("OR")) {
					token = new Token(fieldName, TokenType.OR);
					break;
				}else if(fieldName.equals("LIKE")) {
					token = new Token(fieldName, TokenType.OPERATOR);
					break;
				}else if(fieldName.equals("firstName") || fieldName.equals("lastName") || fieldName.equals("jmbag")) {
					token = new Token(fieldName, TokenType.FIELD);
					break;
				}else throw new RuntimeException("Wrong fieldname!");
			default:
				token = new Token(sb.toString(),type);
				break;
		}
		
		return token;
		
	}
	
	public void skip() {
		for (int i=currentIndex;i<data.length;i++) {
			if (data[i]==' ' || data[i]=='\n' || data[i]=='\t' || data[i]=='\r') continue;
			currentIndex = i;
			return;
		}
		currentIndex = data.length;
	}
	
	public boolean isOperator() {
		
		if (data[currentIndex] == '=' || data[currentIndex] == '>' || data[currentIndex] == '<' || data[currentIndex] == '!') return true;
		return false;
		
	}
	
	public boolean isWhiteSpace() {
		return data[currentIndex] == ' ' || data[currentIndex] == '\r' || data[currentIndex] == '\t' || data[currentIndex] == '\n';
	}
	
	public static void main(String[] args) {
		
		Lexer lexer = new Lexer("jmbag = \"0000000001\" OR firstName LIKE \"*o\"");
		
		Token prvi = lexer.nextToken();
		System.out.println(prvi.getValue()); System.out.println(prvi.getType());
		prvi = lexer.nextToken();
		System.out.println(prvi.getValue()); System.out.println(prvi.getType());
		prvi = lexer.nextToken();
		System.out.println(prvi.getValue()); System.out.println(prvi.getType());
		prvi = lexer.nextToken();
		System.out.println(prvi.getValue()); System.out.println(prvi.getType());
		prvi = lexer.nextToken();
		System.out.println(prvi.getValue()); System.out.println(prvi.getType());
		prvi = lexer.nextToken();
		System.out.println(prvi.getValue()); System.out.println(prvi.getType());
		prvi = lexer.nextToken();
		System.out.println(prvi.getValue()); System.out.println(prvi.getType());
		prvi = lexer.nextToken();
		System.out.println(prvi.getValue()); System.out.println(prvi.getType());
		
	}
	
}
