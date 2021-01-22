package hr.fer.zemris.lsystems.impl;

import java.awt.Color;

import hr.fer.oprpp1.custom.collections.Dictionary;
import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.commands.*;

public class LSystemBuilderImpl implements LSystemBuilder{
	
	private Dictionary<Character, String> productions;
	
	private Dictionary<Character, Command> commands;
	
	private double unitLength = 0.1;
	private double unitLengthDegreeScaler = 1;
	private Vector2D origin = new Vector2D(0,0);
	private double angle = 0;
	private String axiom = "";
	
	public LSystemBuilderImpl() {
		productions = new Dictionary<>();
		commands = new Dictionary<>();
	}
	
	private class LSystemImpl implements LSystem {
		
		public LSystemImpl() {
			super();
		}

		@Override
		public void draw(int depth, Painter painter) {
			
			Context context = new Context();
			
			TurtleState initialState = new TurtleState(origin, new Vector2D(1,0).rotated(angle*Math.PI/180), Color.black, unitLength*Math.pow(unitLengthDegreeScaler, depth));
			context.pushState(initialState);
			String generatedString = generate(depth);
			
			for (int i=0; i<generatedString.length(); i++) {
				
				Command command = commands.get(generatedString.charAt(i));
				
				if (command!=null) {
					command.execute(context, painter);
				}
				
			}
			
		}
		
		@Override
		public String generate(int depth) {
			
			String result = axiom;
			
			StringBuilder sb = new StringBuilder();
			
			for (int i=0; i<depth; i++) {
				
				for (int j=0; j<result.length(); j++) {
					String production = productions.get(result.charAt(j));
					
					if (production!=null) {
						sb.append(production);
					}else {
						sb.append(result.charAt(j));
					}
				}
				
				result = sb.toString();
				sb.setLength(0);
				
			}
			return result;
			
		} 
		
	}
	
	@Override
	public LSystemBuilder setAngle(double angle) {
		this.angle = angle;
		return this;
	}

	@Override
	public LSystemBuilder setAxiom(String axiom) {
		this.axiom = axiom;
		return this;
	}

	@Override
	public LSystemBuilder setOrigin(double x, double y) {
		origin = new Vector2D(x,y);
		return this;
	}

	@Override
	public LSystemBuilder setUnitLength(double unitLength) {
		this.unitLength = unitLength;
		return this;
	}

	@Override
	public LSystemBuilder setUnitLengthDegreeScaler(double scaler) {
		unitLengthDegreeScaler = scaler;
		return this;
	}
	
	@Override
	public LSystemBuilder registerProduction(char c, String s) {
		productions.put(c,s);
		return this;
	}

	@Override
	public LSystem build() {
		return new LSystemImpl();
	}

	@Override
	public LSystemBuilder configureFromText(String[] arrayOfStrings) {
		
		for (String s : arrayOfStrings) {
			
			if (s.equals("")) continue;
			
			String[] array = s.split("\\s+");
			
			if (array[0].equals("origin")) {
				
				if (array.length!=3) throw new RuntimeException();
				
				try {
					origin = new Vector2D(Double.parseDouble(array[1]), Double.parseDouble(array[2]));
				}catch(Exception e) {
					throw new RuntimeException();
				}
				
			}else if (array[0].equals("angle")) {
				
				if (array.length!=2) throw new RuntimeException();
				
				try {
					angle = Double.parseDouble(array[1]);
				}catch(Exception e) {
					throw new RuntimeException();
				}
				
			}else if (array[0].equals("unitLength")) {
				
				if (array.length!=2) throw new RuntimeException();
				
				try {
					unitLength = Double.parseDouble(array[1]);
				}catch(Exception e) {
					throw new RuntimeException();
				}
				
			}else if (array[0].equals("unitLengthDegreeScaler")) {
				
				if (array.length==2) {
					if (array[1].indexOf('/')!=-1) {
						try {
							double first = Double.parseDouble(array[1].substring(0, array[1].indexOf('/')));
							double second = Double.parseDouble(array[1].substring(array[1].indexOf('/')+1));
							unitLengthDegreeScaler = first/second;
						}catch(Exception e) {
							throw new RuntimeException();
						}
					}else {
						try {
							unitLengthDegreeScaler = Double.parseDouble(array[1]);
						}catch(Exception e) {
							throw new RuntimeException();
						}
					}
				}else if(array.length==3) {
					if (array[1].indexOf('/')!=-1) {
						try {
							double first = Double.parseDouble(array[1].substring(0, array[1].indexOf('/')));
							double second = Double.parseDouble(array[2]);
							unitLengthDegreeScaler = first/second;
						}catch(Exception e) {
							throw new RuntimeException();
						}
					}else {
						try {
							double first = Double.parseDouble(array[1]);
							double second = Double.parseDouble(array[2].substring(1));
							unitLengthDegreeScaler = first/second;
						}catch(Exception e) {
							throw new RuntimeException();
						}
					}
				}else if(array.length==4) {
					try {
						unitLengthDegreeScaler = Double.parseDouble(array[1]) / Double.parseDouble(array[3]);
					}catch(Exception e) {
						throw new RuntimeException();
					}
				}else throw new RuntimeException();
				
			}else if(array[0].equals("axiom")) {
				
				if (array.length!=2) throw new RuntimeException();
				axiom = array[1];
				
			}else if(array[0].equals("production")) {
				
				if (array.length!=3 || array[1].length()!=1) throw new RuntimeException();
				
				try {
					productions.put(array[1].charAt(0), array[2]);
				}catch(Exception e) {
					throw new RuntimeException();
				}
						
			}else if(array[0].equals("command")) {
				
				if (array[1].length()!=1 || (array.length!=3 && array.length!=4)) throw new RuntimeException();
				
				char c = array[1].charAt(0);
				String command = array[2];
				
				if (array.length==4) command += " " + array[3];
				
				registerCommand(c, command);
				
			}else throw new RuntimeException();
			
		}
		
		return this;
		
	}
	
	

	@Override
	public LSystemBuilder registerCommand(char c, String s) {
		
		String[] array = s.split("\\s+");
		
		if (array[0].equals("draw")) {
			
			try {
                DrawCommand dc = new DrawCommand(Double.parseDouble(array[1]));
                commands.put(c, dc);
            } catch (Exception e) {
                throw new RuntimeException();
            }
			
		}else if(array[0].equals("skip")) {
			
			try {
				SkipCommand sc = new SkipCommand(Double.parseDouble(array[1]));
				commands.put(c, sc);
			}catch(Exception e) {
				throw new RuntimeException();
			}
			
		}else if(array[0].equals("scale")) {
			
			try {
				ScaleCommand sc = new ScaleCommand(Double.parseDouble(array[1]));
				commands.put(c, sc);
			}catch(Exception e) {
				throw new RuntimeException();
			}
			
		}else if(array[0].equals("rotate")) {
			
			try {
				RotateCommand rc = new RotateCommand( Math.PI/180 * Double.parseDouble(array[1]));
				commands.put(c, rc);
			}catch(Exception e) {
				throw new RuntimeException();
			}
			
		}else if(array[0].equals("push")) {
			commands.put(c, new PushCommand());
		}else if(array[0].equals("pop")) {
			commands.put(c,  new PopCommand());
		}else if(array[0].equals("color")) {
			
			try {
				Color color = Color.decode("0x" + array[1]);
				ColorCommand cc = new ColorCommand(color);
				commands.put(c, cc);
			}catch(Exception e) {
				throw new RuntimeException();
			}
			
		}else throw new RuntimeException();
		
		return this;
		
	}

}























































