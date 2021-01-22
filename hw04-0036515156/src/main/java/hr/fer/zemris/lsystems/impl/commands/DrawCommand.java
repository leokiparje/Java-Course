package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

public class DrawCommand implements Command {
	
	private double shiftLength;
	
	public DrawCommand(double length) {
		shiftLength = length;
	}
	
	public double getShiftLength(){
		return shiftLength;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		
		TurtleState currentState = ctx.getCurrentState();
		
		Vector2D currentAngle = currentState.getAngle();
		
		Vector2D currentPosition = currentState.getPosition();
		
		double length = currentState.getShiftLength();
		
		Vector2D newVector = currentPosition.added(currentAngle.scaled(shiftLength*length));
		
		painter.drawLine(currentPosition.getX(), currentPosition.getY(), newVector.getX(), newVector.getY(), currentState.getColor(), 1);
		
		currentState.setPosition(newVector);
		
	}

}
