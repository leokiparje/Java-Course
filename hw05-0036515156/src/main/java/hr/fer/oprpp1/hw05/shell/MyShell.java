package hr.fer.oprpp1.hw05.shell;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Class MyShell is the main class of this project. It starts the shell.
 * @author leokiparje
 *
 */

public class MyShell {
	
	/*
	 * Main Method
	 */
	
	public static void main(String[] args) {
		
		ShellEnvironment se = new ShellEnvironment();
		ShellStatus status = ShellStatus.CONTINUE;
		
		do {
			
			String l = se.readLine();		
			String commandName = null;
			
			if (!(l.contains(" "))) {
				commandName = l;
			}else {
				commandName = l.substring(0, l.indexOf(" "));
			}

			String arguments = l.substring(commandName.length()).trim();
		
			ShellCommand command = se.commands().get(commandName);	
			
			if (command==null) {
				se.writeln("Command not found, for list of available commands try : help");
				se.write(se.getPromptSymbol()+" ");
				continue;
			}
			status = command.executeCommand(se, arguments);
			
		}while(status==ShellStatus.CONTINUE);
		
			
	}
	
}























































































































