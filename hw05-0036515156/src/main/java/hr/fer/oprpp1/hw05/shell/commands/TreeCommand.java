package hr.fer.oprpp1.hw05.shell.commands;

import java.io.File;
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
 * Tree command has its own description.
 * @author leokiparje
 *
 */

public class TreeCommand implements ShellCommand {
	
	 StringBuilder sb = new StringBuilder();

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		
		if (arguments.length()<1) {
			env.writeln("Tree command takes a single argument but none were given.");
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
			env.writeln("Tree command takes a single argument but more were given.");
			env.write(env.getPromptSymbol()+" ");
			return ShellStatus.CONTINUE;
		}
		
		Path path = Paths.get(p);
		
		if (!Files.isDirectory(path)) {
            env.writeln("Tree commands works only with directories but given argument wasn't one.");
            env.write(env.getPromptSymbol() + " ");
            return ShellStatus.CONTINUE;
        }
		
		try {
			env.write(walk(path.toString(),0));
		}catch(Exception e) {
			env.writeln("Unable to walk file tree.");
			env.write(env.getPromptSymbol()+" ");
			return ShellStatus.CONTINUE;
			
		}
		
		env.write(env.getPromptSymbol()+" ");
		return ShellStatus.CONTINUE;
		
	}

	@Override
	public String getCommandName() {
		return "tree";
	}

	@Override
	public List<String> getCommandDescription() {
		
		List<String> description = new ArrayList<>();
		
		description.add("Takes a single argument : directory name.");
		description.add("Prints a tree of that directory.");
		description.add("Each directory level shifs output two characters to right.");
		
		return description;
		
	}
	
	public String spaces(int n) {
		StringBuilder sb = new StringBuilder();
		for (int i=0;i<n;i++) {
			sb.append(" ");
		}
		return sb.toString();
	}
	
	public String walk(String path, int razina) {
		
		File file = new File(path);
        File[] fileList = file.listFiles();
        
        if (fileList==null) return "";
        
        for (File f : fileList) {
        	
        	sb.append(("  ".repeat(razina)+f.getName()+"\n"));
        	walk(f.getAbsolutePath(), razina+1);
        	
        }
        
        return sb.toString();
        
	}

}






















































