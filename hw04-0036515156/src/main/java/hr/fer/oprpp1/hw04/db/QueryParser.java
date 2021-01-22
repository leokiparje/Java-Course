package hr.fer.oprpp1.hw04.db;

import java.util.*;

/**
 * Class QueryParser parses query that is given in constructor
 * @author leokiparje
 *
 */

public class QueryParser {
	
	String query;
	
	/**
	 * private variable Lexer
	 */
	
	private Lexer lexer;
	
	/*
	 * private inner List of expressions
	 */
	
	private List<List<ConditionalExpression>> listQueries;
	
	/*
	 * basic constructor
	 */
	
	public QueryParser(String query) {
		this.query = query;
		lexer = new Lexer(query);
		listQueries = new ArrayList<>();
		parse();
	}
	
	/*
	 * Method checks if query is a direct one
	 */
	
	public boolean isDirectQuery() {
		
		if (listQueries.size()!=1) return false;
		
		ConditionalExpression expression = listQueries.get(0).get(0);
		
		return expression.getFieldGetter()==FieldValueGetters.JMBAG && expression.getComparisonOperator()==ComparisonOperators.EQUALS;
		
	}
	
	public boolean isOrQuery() {
		
		boolean hasAnd = false;
		boolean hasOr = false;
		
		if (query.indexOf("OR")!=-1) hasOr = true;
		if (query.indexOf("AND")!=-1) hasAnd = true;
		
		return hasOr && !hasAnd;
		
	}
	
	public boolean isAndQuery() {
		
		boolean hasAnd = false;
		boolean hasOr = false;
		
		if (query.indexOf("OR")!=-1) hasOr = true;
		if (query.indexOf("AND")!=-1) hasAnd = true;
		
		return !hasOr && hasAnd;
		
	}
	
	public boolean isSimpleQuery() {
		
		boolean hasAnd = false;
		boolean hasOr = false;
		
		if (query.indexOf("OR")!=-1) hasOr = true;
		if (query.indexOf("AND")!=-1) hasAnd = true;
		
		return !hasOr && !hasAnd;
		
	}
	
	/*
	 * Method returns String of jmbag that is being queried
	 */
	
	public String getQueriedJmbag() {
		
		if (!isDirectQuery()) throw new IllegalStateException();
		
		return listQueries.get(0).get(0).getStringLiteral();
		
	}
	
	/*
	 * Returns query list
	 */
	
	public List<List<ConditionalExpression>> getQuery(){
		return listQueries;
	}
	
	/*
	 * parses query
	 */
	
	public void parse() {
		
		Token trenutni = lexer.nextToken();
		
		List<ConditionalExpression> list = new ArrayList<>();
		
		while(true) {
			
			if (trenutni.getType()==TokenType.EOF) break;
			
			if (trenutni.getType()!=TokenType.FIELD) throw new RuntimeException("Prvi token mora biti field!");
				
			IFieldValueGetter field;
			String fieldName = trenutni.getValue().toString();
			
			if (fieldName.equals("firstName")) {
				field = FieldValueGetters.FIRST_NAME;
			}else if (fieldName.equals("lastName")) {
				field = FieldValueGetters.LAST_NAME;
			}else if (fieldName.contentEquals("jmbag")) {
				field = FieldValueGetters.JMBAG;
			}else throw new RuntimeException("Kirvo zadan query!");
			
			trenutni = lexer.nextToken();
			
			if (trenutni.getType()!=TokenType.OPERATOR) throw new RuntimeException("Drugi token mora biti operator!");
			
			IComparisonOperator operator;
			String co = trenutni.getValue().toString();
			
			if (co.equals("=")) {
				operator = ComparisonOperators.EQUALS;
			}else if (co.equals(">")) {
				operator = ComparisonOperators.GREATER;
			}else if (co.equals("<")) {
				operator = ComparisonOperators.LESS;
			}else if (co.equals("!=")) {
				operator = ComparisonOperators.NOT_EQUALS;
			}else if (co.equals(">=")) {
				operator = ComparisonOperators.GREATER_OR_EQUALS;
			}else if (co.equals("<=")) {
				operator = ComparisonOperators.LESS_OR_EQUALS;
			}else if (co.equals("LIKE")) {
				operator = ComparisonOperators.LIKE;
			}else throw new RuntimeException("Krivo zadan query!");
			
			trenutni = lexer.nextToken();
			
			if (trenutni.getType()!=TokenType.STRING) throw new RuntimeException("Treći token mora biti string!");
			
			String literal = trenutni.getValue().toString();
			
			ConditionalExpression ekspresija = new ConditionalExpression(field, literal, operator);
			
			trenutni = lexer.nextToken();
			
			if (trenutni.getType()==TokenType.AND) {
				list.add(ekspresija);
				trenutni = lexer.nextToken();
			}else if (trenutni.getType()==TokenType.OR) {
				list.add(ekspresija);
				List<ConditionalExpression> l = new ArrayList<>(list);
				listQueries.add(l);
				list.clear();
				trenutni = lexer.nextToken();
			}else {
				list.add(ekspresija);
				List<ConditionalExpression> l = new ArrayList<>(list);
				listQueries.add(l);
				list.clear();
				break;
			}
			
		}
		
	}
	
}
