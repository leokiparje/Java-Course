package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

public class RotateCommand implements Command{
	
	private double angle;
	
	public RotateCommand(double angle) {
		this.angle = angle;
	}
	
	public double getAngle() {
		return angle;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		
		TurtleState currentState = ctx.getCurrentState();
		
		currentState.setAngle(currentState.getAngle().rotated(angle));
		
	}

}
