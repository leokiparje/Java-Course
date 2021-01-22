package hr.fer.zemris.java.gui.charts;

/**
 * Class XYValue represents one class with 2 read only parameters.
 * @author leokiparje
 *
 */

public class XYValue {
	
	private int x;
	private int y;
	
	/*
	 * Basic constructor
	 */
	
	public XYValue(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	/*
	 * Method parse parses the given String and returns XYValue object
	 */
	
	public static XYValue parse(String s) {
		
		s = s.trim();
		
		String[] brojevi = s.split(",");
		
		int xx = Integer.parseInt(brojevi[0]);
		
		int yy = Integer.parseInt(brojevi[1]);
		
		return new XYValue(xx,yy);
		
	}
	
	public String toString() {
		return x+" "+y;
	}
	
}
