package hr.fer.oprpp1.hw05.shell.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * Help command has its own description.
 * @author leokiparje
 *
 */

public class HelpCommand implements ShellCommand{

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		
		String[] args = arguments.split(" ");
		if (args.length>1) {
			env.writeln("help command takes a single argument or none arguments but more were given.");
			env.write(env.getPromptSymbol()+" ");
			return ShellStatus.CONTINUE;
		}
		
		SortedMap<String, ShellCommand> commands = env.commands();
		
		if (args[0].equals("")) {
			for (ShellCommand c : commands.values()) {
				env.writeln(c.getCommandName());
			}
		}else {
			for (String c : commands.keySet()) {
				if (c.equals(arguments)) {
					StringBuilder sb = new StringBuilder();
					for (String descriptionLine : commands.get(c).getCommandDescription()) {
						sb.append(descriptionLine+" ");
					}
					sb.setLength(sb.length()-1);
					
					env.writeln(c+" : "+sb.toString());					
					env.write(env.getPromptSymbol()+" ");
					return ShellStatus.CONTINUE;
				}
			}
			env.writeln("No such command is supported in this so called shell.");
			env.write(env.getPromptSymbol()+" ");
			return ShellStatus.CONTINUE;
		}
		env.write(env.getPromptSymbol()+" ");
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "help";
	}

	@Override
	public List<String> getCommandDescription() {
		
		List<String> description = new ArrayList<>();
		
		description.add("If started with no arguments it lists names of all supported commands.");
		description.add("If started with single argument it must print name and the description of selected command "
				+ "or print appropriate error message if no such command exists.");
		
		return description;
		
	}
	
	
	
}
