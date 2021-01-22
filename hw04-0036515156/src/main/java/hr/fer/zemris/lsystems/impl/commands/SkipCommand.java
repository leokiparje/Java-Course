package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

public class SkipCommand implements Command{
	
	private double skipLength;
	
	public SkipCommand(double skipLength){
		this.skipLength = skipLength;
	}
	
	public double getSkipLength() {
		return skipLength;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		
		TurtleState currentState = ctx.getCurrentState();
		
		Vector2D currentPosition = currentState.getPosition();
		
		Vector2D currentAngle = currentState.getAngle();
		
		double length = currentState.getShiftLength();
		
		Vector2D newVector = currentPosition.added(currentAngle.scaled(skipLength*length));
		
		currentState.setPosition(newVector);
		
	}

}
