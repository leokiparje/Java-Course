package hr.fer.oprpp1.math;

public class Vector2D {
	
	private double x;
	
	private double y;
	
	public Vector2D(double x, double y) {
		this.x=x;
		this.y=y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void add(Vector2D offset) {
		this.x += offset.getX();
		this.y += offset.getY();
	}
	
	public Vector2D added(Vector2D offset) {
		
		return new Vector2D(this.x+offset.getX(),this.y+offset.getY());
		
	}
	
	public void rotate(double angle) {
		
		double praviX = x;
		
		this.x = Math.cos(angle)*x - Math.sin(angle)*y;
		this.y = Math.sin(angle)*praviX + Math.cos(angle)*y;
		
	}
	
	public Vector2D rotated(double angle) {
		
		return new Vector2D(Math.cos(angle)*x - Math.sin(angle)*y, Math.sin(angle)*x + Math.cos(angle)*y);
		
	}
	
	public void scale(double scaler) {
		
		x*=scaler;
		y*=scaler;
		
	}
	
	public Vector2D scaled(double scaler) {
		
		return new Vector2D(x*scaler,y*scaler);
		
	}
	
	public Vector2D copy() {
		
		return new Vector2D(x,y);
		
	}
	
}
