package hr.fer.oprpp1.java.gui.layouts;

/**
 * Class RCPosition represents a position in CalcLayout grid which has 7 columns and 5 rows.
 * @author leokiparje
 *
 */

public class RCPosition {

	private int row;
	private int column;
	
	public RCPosition(int r, int c) {
		if ((r<1 || r>7) || (c<1 || c>7)) throw new CalcLayoutException("Illegal position.");
		if (r==1 && (c>1 && c<6)) throw new CalcLayoutException("Positions (1,2) to (1,5) are not legal.");
		row = r;
		column = c;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	/**
	 * Method parse of RCPositon class is used to parse the String and return a RCPosition object if possible
	 * @param text
	 * @return
	 */
	
	public static RCPosition parse(String text) {
		
		int r=0;
		int c=0;
		text = text.trim();
		try {
			r = Integer.parseInt(text.substring(0,1));
			if (text.indexOf(",")==-1) throw new CalcLayoutException("Wrong format of position.");
			c = Integer.parseInt(text.substring(text.indexOf(",")+1));
		}catch(Exception e) {
			System.out.println(e);
		}
		return new RCPosition(r,c);
		
	}
}
