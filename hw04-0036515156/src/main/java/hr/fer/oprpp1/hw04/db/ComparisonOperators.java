package hr.fer.oprpp1.hw04.db;

/**
 * Class ComparisonOperators represents comprasion operators
 * @author leokiparje
 *
 */

public class ComparisonOperators {
	
	/*
	 * Each of the public static final IComparisonOperator implementations retunrs appropriate boolean value 
	 */
	
	public static final IComparisonOperator LESS = (v1,v2) -> v1.compareTo(v2)<0;
	public static final IComparisonOperator LESS_OR_EQUALS = (v1,v2) -> v1.compareTo(v2)<=0;
	public static final IComparisonOperator GREATER = (v1,v2) -> v1.compareTo(v2)>0;
	public static final IComparisonOperator GREATER_OR_EQUALS = (v1,v2) -> v1.compareTo(v2)>=0;
	public static final IComparisonOperator EQUALS = (v1,v2) -> v1.compareTo(v2)==0;
	public static final IComparisonOperator NOT_EQUALS = (v1,v2) -> v1.compareTo(v2)!=0;
	
	public static final IComparisonOperator LIKE = (v1,v2) -> {
		int index = v2.indexOf('*');
		if (index==-1) {
			return v1.compareTo(v2)==0;
		}else if(index==0 && v2.length()==1) return true;
		
		if (index==0) {
			if (v2.substring(1).contains("*")) throw new RuntimeException("Too many wild cards!");
			return v1.substring(v1.length()-v2.substring(1).length()).compareTo(v2.substring(1))==0;
		}
		
		for (char c : v2.toCharArray()) {
			if (c!='*') continue;
			if (v2.substring(index+1).contains("*")) throw new RuntimeException("Too many wild cards!");
			if (!v1.substring(0,index).equals(v2.substring(0,index))) return false;
			if (index==v2.length()-1) return true;
			if (v2.substring(index+1).length()>v1.substring(index).length()) return false;
			return v1.substring(v1.length()-v2.substring(index+1).length()).compareTo(v2.substring(index+1))==0;
		}
		return true;
	};

}
