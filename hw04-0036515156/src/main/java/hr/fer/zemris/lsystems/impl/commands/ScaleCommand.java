package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

public class ScaleCommand implements Command{
	
	private double scaler;
	
	public ScaleCommand(double scaler) {
		this.scaler = scaler;
	}
	
	public double getScaler() {
		return scaler;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		
		TurtleState currentState = ctx.getCurrentState();
		
		currentState.setShiftLength(currentState.getShiftLength()*scaler);
		
	}

}
