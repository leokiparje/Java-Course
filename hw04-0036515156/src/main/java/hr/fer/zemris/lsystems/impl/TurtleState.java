package hr.fer.zemris.lsystems.impl;

import java.awt.Color;

import hr.fer.oprpp1.math.Vector2D;

public class TurtleState {
	
	Vector2D currentPosition;
	
	/**
	 * Jedinični vektor smjera
	 */
	Vector2D currentAngle;
	
	Color currentColor;
	
	double currentShiftLength;
	
	public TurtleState(Vector2D position, Vector2D angle, Color color, double shift) {
		currentPosition = position;
		currentAngle = angle;
		currentColor = color;
		currentShiftLength = shift;
	}
	
	public TurtleState copy() {
		
		return new TurtleState(currentPosition.copy(), currentAngle.copy(), new Color(currentColor.getRGB()), currentShiftLength);
		
	}
	
	public void setPosition(Vector2D position) {
		currentPosition = position;
	}
	
	public void setAngle(Vector2D angle) {
		currentAngle = angle;
	}
	
	public void setColor(Color color) {
		currentColor = color;
	}
	
	public void setShiftLength(double shiftLength) {
		currentShiftLength = shiftLength;
	}
	
	public Vector2D getPosition() {
		return currentPosition;
	}
	
	public Vector2D getAngle() {
		return currentAngle;
	}
	
	public Color getColor() {
		return currentColor;
	}
	public double getShiftLength() {
		return currentShiftLength;
	}
	
}
