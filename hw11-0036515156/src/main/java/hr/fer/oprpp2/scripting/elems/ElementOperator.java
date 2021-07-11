package hr.fer.oprpp2.scripting.elems;

/**
 * Class ElementOperator represents Element with value string.
 * @author leokiparje
 *
 */

public class ElementOperator extends Element {
	
	/**
	 * symbol string
	 */
	
		private String symbol;
		
		/**
		 * constructor with String argument
		 * @param symbol String argument
		 */
		
		public ElementOperator(String symbol) {
			if (symbol==null) throw new NullPointerException();
			this.symbol=symbol;
		}
		
		/**
		 * returns String representation of Element
		 */
		
		public String asText() {
			return symbol;
		}
}
