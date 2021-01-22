package hr.fer.oprpp1.hw05.shell.commands;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * Charsets command has its own description.
 * @author leokiparje
 *
 */

public class CharsetsCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		
		if (arguments.length()!=0) {
			env.writeln("Command charsets take no arguments but some were given.");
			env.write(env.getPromptSymbol()+" ");
			return ShellStatus.CONTINUE;
		}
		
		SortedMap<String,Charset> charsetsMap = Charset.availableCharsets();
		
		for (Map.Entry<String, Charset> entry : charsetsMap.entrySet()) {
			env.writeln(entry.getKey());
		}
		
		env.write(env.getPromptSymbol()+" ");
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "charsets";
	}

	@Override
	public List<String> getCommandDescription() {
		
		List<String> description = new ArrayList<>();
		
		description.add("Takes no arguments.");
		description.add("Lists names of supported charsets for this Java plarform.");
		description.add("A single charset name is written per line.");
		
		return description;
		
	}

}
