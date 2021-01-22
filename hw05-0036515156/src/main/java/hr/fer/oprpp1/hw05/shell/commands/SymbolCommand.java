package hr.fer.oprpp1.hw05.shell.commands;

import java.util.ArrayList;
import java.util.List;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * Symbol command has its own description.
 * @author leokiparje
 *
 */

public class SymbolCommand implements ShellCommand {
	
	List<String> symbols = new ArrayList<>();

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		
		initializeList();
		
		String[] args = arguments.split(" ");
		if (args.length<1 || args.length>2) {
			env.writeln("Symbol command takes one or two arguments but none or more than two were given.");
			env.write(env.getPromptSymbol()+" ");
			return ShellStatus.CONTINUE;
		}
		
		String symbolName = args[0];
		
		if (args.length==1) {
			if (!symbols.contains(symbolName)) {
				env.writeln("Wrong symbol name is given. Symbols available are : PROMPT, MORELINES, MULTILINE.");
				env.write(env.getPromptSymbol()+" ");
				return ShellStatus.CONTINUE;
			}
			String currentSymbol=null;
			switch(symbolName) {
				case "PROMPT":
					currentSymbol = env.getPromptSymbol().toString();
					break;
				case "MORELINES":
					currentSymbol = env.getMorelinesSymbol().toString();
					break;
				case "MULTILINE":
					currentSymbol = env.getMultilineSymbol().toString();
					break;
			}
			env.writeln("Symbol for "+symbolName+" is '"+currentSymbol+"'");
			env.write(env.getPromptSymbol()+" ");
			return ShellStatus.CONTINUE;
		}else {
			String newSymbol = args[1];
			Character oldSymbol = env.getPromptSymbol();
			switch(symbolName) {
				case "PROMPT":
					env.setPromptSymbol(newSymbol.charAt(0));
					env.writeln("Symbol for "+symbolName+" changed from '"+oldSymbol+"' to '"+env.getPromptSymbol()+"'");
					break;
				case "MORELINES":
					env.setMorelinesSymbol(((newSymbol.charAt(0))));
					env.writeln("Symbol for "+symbolName+" changed from '"+oldSymbol+"' to '"+env.getMorelinesSymbol()+"'");
					break;
				case "MULTILINE":
					env.setMultilineSymbol((newSymbol.charAt(0)));
					env.writeln("Symbol for "+symbolName+" changed from '"+oldSymbol+"' to '"+env.getMultilineSymbol()+"'");
					break;
			}
		}
		env.write(env.getPromptSymbol()+" ");
		return ShellStatus.CONTINUE;
		
	}

	@Override
	public String getCommandName() {
		return "symbol";
	}

	@Override
	public List<String> getCommandDescription() {
		
		List<String> description = new ArrayList<>();
		
		description.add("Takes one or two argument.");
		description.add("If only one argument is given then console displays what that symbol is in this particular so called shell.");
		description.add("If two arguments are given then shell changes symbol from what it was before to second argument.");
		
		return description;
		
	}
	
	public void initializeList() {
		symbols.add("PROMPT");
		symbols.add("MORELINES");
		symbols.add("MULTILINE");
	}

}
