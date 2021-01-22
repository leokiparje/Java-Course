package hr.fer.oprpp1.hw05.shell.commands;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ParsePath;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * Mkdir command has its own description.
 * @author leokiparje
 *
 */

public class MkdirCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		
		if (arguments.length()<1) {
			env.writeln("Mkdir command takes a single argument but none were given.");
			env.write(env.getPromptSymbol()+" ");
			return ShellStatus.CONTINUE;
		}
		String p = null;
		try {
			p = ParsePath.parse(arguments);
		}catch(Exception e) {
			env.writeln("Wrong argument format given.");
			env.write(env.getPromptSymbol()+" ");
			return ShellStatus.CONTINUE;
		}
		int pathLength = p.length();
		
		if (arguments.startsWith("\"")) {
			pathLength+=2;
		}
		
		if (pathLength < arguments.length()) {
			env.writeln("Mkdir command takes a single argument but more were given.");
			env.write(env.getPromptSymbol()+" ");
			return ShellStatus.CONTINUE;
		}
		
		Path path = Paths.get(p);
		
		try {
			Files.createDirectories(path);
			env.writeln("Directory sucessfully created.");
		}catch(Exception e) {
			env.writeln(("Unable to create a new directory."));
		}
		
		env.write(env.getPromptSymbol() + " ");
		return ShellStatus.CONTINUE;
		
	}

	@Override
	public String getCommandName() {
		return "mkdir";
	}

	@Override
	public List<String> getCommandDescription() {
		
		List<String> description = new ArrayList<>();
		
		description.add("Takes a single argument : directory name.");
		description.add("Creates the appropriate directory structure.");
		
		return description;
		
	}

}
