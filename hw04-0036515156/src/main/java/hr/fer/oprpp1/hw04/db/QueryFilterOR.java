package hr.fer.oprpp1.hw04.db;

import java.util.List;

public class QueryFilterOR implements IFilter{
	
	private List<ConditionalExpression> queryList;
	
	public QueryFilterOR(List<ConditionalExpression> queryList) {
		 this.queryList=queryList;
	}

	@Override
	public boolean accepts(StudentRecord record) {
		
		for (ConditionalExpression expression : queryList) {
			
			if (expression.getFieldGetter()==FieldValueGetters.FIRST_NAME) {
				if (expression.getComparisonOperator().satisfied(record.getFirstName(), expression.getStringLiteral())) return true;
			}else if(expression.getFieldGetter()==FieldValueGetters.LAST_NAME) {
				if (expression.getComparisonOperator().satisfied(record.getLastName(), expression.getStringLiteral())) return true;
			}else if(expression.getFieldGetter()==FieldValueGetters.JMBAG) {
				if (expression.getComparisonOperator().satisfied(record.getJmbag(), expression.getStringLiteral())) return true;
			}
			
		}
		
		return false;
		
	}

}
