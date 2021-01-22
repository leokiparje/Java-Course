package hr.fer.oprpp1.hw05.shell.commands;

import java.util.ArrayList;
import java.util.List;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * Exit command has its own description.
 * @author leokiparje
 *
 */

public class ExitCommand implements ShellCommand{

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {		
		
		if (arguments.length()!=0) {
			env.writeln("Exit command takes no arguments.");
			env.write(env.getPromptSymbol() + " ");
            return ShellStatus.CONTINUE;
		}
		
		return ShellStatus.TERMINATE;
	}

	@Override
	public String getCommandName() {
		return "exit";
	}

	@Override
	public List<String> getCommandDescription() {
		
		List<String> description = new ArrayList<>();
		
		description.add("Exit command takes no arguments.");
		description.add("After executing this command shell terminates.");
		
		return description;
		
	}

}
