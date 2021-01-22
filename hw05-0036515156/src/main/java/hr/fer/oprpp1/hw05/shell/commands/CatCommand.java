package hr.fer.oprpp1.hw05.shell.commands;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ParsePath;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * Cat command has its own description.
 * @author leokiparje
 *
 */

public class CatCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		
		if (arguments.length()<1) {
			env.writeln("Cat command takes one or two argument but none were given.");
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
		
		Path path = Paths.get(p);		
		Charset charset = Charset.defaultCharset();
		if (arguments.length()>pathLength) {
			try {
				charset = Charset.forName(ParsePath.parse((arguments.substring(pathLength)).trim()));
			}catch(Exception e) {
				env.writeln("Charset failed to parse.");
				env.write(env.getPromptSymbol()+" ");
				return ShellStatus.CONTINUE;
			}
			
			String p3 = null;
			if (arguments.length()>pathLength+charset.toString().length()+1) {
				try {
					p3 = ParsePath.parse((arguments.substring(p.length()+charset.toString().length())+1).trim());
					if (!p3.equals("")) {
						env.writeln("Copy command takes two arguments but more were given.");
						env.write(env.getPromptSymbol()+" ");
						return ShellStatus.CONTINUE;
					}
				}catch(Exception e) {
					env.writeln("Wrong argument format given.");
					env.write(env.getPromptSymbol()+" ");
					return ShellStatus.CONTINUE;
				}
			}
		}
		
		try(InputStream is = Files.newInputStream(path)){
			
			byte[] buff = new byte[4096];
			
			while(true) {
				
				int r = is.read(buff);		
				if (r<1) break;
				
				String result = new String(Arrays.copyOf(buff, r), charset);
				env.write(result);
				
			}
			env.write(env.getPromptSymbol()+" ");
            return ShellStatus.CONTINUE;
			
		}catch(Exception e) {
			env.writeln(("Unable to read from input stream."));
			env.write(env.getPromptSymbol() + " ");
            return ShellStatus.CONTINUE;
		}
		
		
		
	}

	@Override
	public String getCommandName() {
		return "cat";
	}

	@Override
	public List<String> getCommandDescription() {
		
		List<String> description = new ArrayList<>();
		
		description.add("Takes on or two arguments.");
		description.add("The first argument is path to some file and is mandatory.");
		description.add("The second argument is charset name that should be used to interpret chars from bytes.");
		description.add("If not second argument is not provided, a default platform charset should be used");
		description.add("This command opens given file and writes its content to console.");
		
		return description;
		
	}
	
}
