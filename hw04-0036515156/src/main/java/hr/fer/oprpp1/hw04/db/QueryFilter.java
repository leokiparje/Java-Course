package hr.fer.oprpp1.hw04.db;

import java.util.*;

/**
 * Class QueryFilter implements query filter class
 * @author leokiparje
 *
 */

public class QueryFilter implements IFilter {
	
	/**
	 * Inner List of expressions
	 */
	
	private List<ConditionalExpression> queryList;
	
	/*
	 * default constructor
	 */
	
	public QueryFilter(List<ConditionalExpression> queryList) {
		 this.queryList=queryList;
	}
	
	/*
	 * method has one argument StudentRecord and if that record satisfies expressions it retunrs true, else it returns false
	 */

	@Override
	public boolean accepts(StudentRecord record) {
		
		for (ConditionalExpression expression : queryList) {
			
			if (expression.getFieldGetter()==FieldValueGetters.FIRST_NAME) {
				if (!expression.getComparisonOperator().satisfied(record.getFirstName(), expression.getStringLiteral())) return false;
			}else if(expression.getFieldGetter()==FieldValueGetters.LAST_NAME) {
				if (!expression.getComparisonOperator().satisfied(record.getLastName(), expression.getStringLiteral())) return false;
			}else if(expression.getFieldGetter()==FieldValueGetters.JMBAG) {
				if (!expression.getComparisonOperator().satisfied(record.getJmbag(), expression.getStringLiteral())) return false;
			}
		
		}
		
		return true;
	}
		
}
