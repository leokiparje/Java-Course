package hr.fer.oprpp1.hw04.db;

/**
 * Class ConditionalExpression represents conditional expression
 * @author leokiparje
 *
 */

public class ConditionalExpression {
	
	/*
	 * IFieldValueGetter fieldValue
	 */
	
	IFieldValueGetter fieldValue;
	
	/*
	 * String literal
	 */
	
	String literal;
	
	/*
	 * IComparisonOperator operator
	 */
	
	IComparisonOperator operator;
	
	/*
	 * Basic constructor
	 */
	
	public ConditionalExpression(IFieldValueGetter fieldValue, String literal, IComparisonOperator operator) {
		this.fieldValue = fieldValue;
		this.literal = literal;
		this.operator = operator;
	}
	
	public IComparisonOperator getComparisonOperator() {
		return operator;
	}
	
	public IFieldValueGetter getFieldGetter() {
		return fieldValue;
	}
	
	public String getStringLiteral() {
		return literal;
	}
	
}
