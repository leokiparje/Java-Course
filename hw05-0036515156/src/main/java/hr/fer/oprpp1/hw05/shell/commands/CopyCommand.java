package hr.fer.oprpp1.hw05.shell.commands;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ParsePath;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * Copy command has its own description.
 * @author leokiparje
 *
 */

public class CopyCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
			
		if (arguments.length()<1) {
			env.writeln("Hexdump command takes a single argument but none were given.");
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
		
		if (pathLength>=arguments.length()) {
			env.writeln("Copy command takes two arguments but only one was given.");
			env.write(env.getPromptSymbol()+" ");
			return ShellStatus.CONTINUE;
		}
		
		Path source = Paths.get(p);
		String p2 = null;
		
		try {
			p2 = ParsePath.parse((arguments.substring(pathLength)).trim());
		}catch(Exception e) {
			env.writeln("Wrong argument format given.");
			env.write(env.getPromptSymbol()+" ");
			return ShellStatus.CONTINUE;
		}
		
		int path2Length = p2.length();
		if (arguments.substring(pathLength).trim().startsWith("\"")) {
			path2Length+=2;
		}
		
		Path destinationPath = Paths.get(p2);
		
		if (source.equals(destinationPath)) {
			env.writeln("File can't be copied to itself.");
			env.write(env.getPromptSymbol()+" ");
			return ShellStatus.CONTINUE;
		}
		
		String p3 = null;
		
		try {
			p3 = ParsePath.parse((arguments.substring(pathLength+path2Length+1)).trim());
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
		
		String answer = null;
		
		if (!Files.exists(source)) {
			env.writeln("Source file coudn't be found.");
			env.write(env.getPromptSymbol() + " ");
			return ShellStatus.CONTINUE;
		}
		
		if (Files.isDirectory(source)) {
			env.writeln("Copy command can only copy files but directory was given.");
			env.write(env.getPromptSymbol() + " ");
			return ShellStatus.CONTINUE;
		}
		if (Files.exists(destinationPath)) {

			if (Files.isDirectory(destinationPath)) {
				File newFile = new File(destinationPath.toString()+File.separator+source.getFileName());
				newFile.getParentFile().mkdirs();
				try {
					newFile.createNewFile();
					destinationPath = Paths.get(newFile.getAbsolutePath());
					answer = "y";
				}catch(Exception e) {
					env.writeln("Unable to create a new file.");
					env.write(env.getPromptSymbol()+" ");
					return ShellStatus.CONTINUE;
				}		
			}else {
				
				env.writeln("Do you want to override the current file?(Y/N)");
				env.write(env.getPromptSymbol() + " ");
				answer = env.readLine();
				
			}
			
			if (answer.equalsIgnoreCase("y")) {
				
				InputStream is = null;
				OutputStream os = null;
				
				try {
					is = Files.newInputStream(source);
					os = Files.newOutputStream(destinationPath, StandardOpenOption.TRUNCATE_EXISTING);
				}catch(Exception e) {
					System.out.println(e);
				}
				
				byte[] buff = new byte[4096];
				
				while(true) {
					try {
						int r = is.read(buff);
						if (r<1) break;	
						os.write(Arrays.copyOf(buff, r));
					}catch(Exception e) {
						env.writeln("Unable to read the buffer!");
						env.write(env.getPromptSymbol() + " ");
			            return ShellStatus.CONTINUE;
					}
					
				}
				env.writeln("File copied successfully.");
				env.write(env.getPromptSymbol() + " ");
		        return ShellStatus.CONTINUE;
				
			}else if(answer.equalsIgnoreCase("n")) {
				env.writeln("Copying wasn't done.");
				env.write(env.getPromptSymbol() + " ");
				return ShellStatus.CONTINUE;
			}else {
				env.writeln("Y or N expected but none were given. Terminating command.");
				env.write(env.getPromptSymbol() + " ");
				return ShellStatus.CONTINUE;
			}
		}
		
		env.writeln("Destination file coudn't be found.");
		env.write(env.getPromptSymbol() + " ");
		return ShellStatus.CONTINUE;
		
	}

	@Override
	public String getCommandName() {
		return "copy";
	}

	@Override
	public List<String> getCommandDescription() {
		
		List<String> description = new ArrayList<>();
		
		description.add("Takes a single argument : directory.");
		description.add("Writes a directory listing(not recursive).");
		description.add("The output consists of 4 columns.");
		description.add("First column indicates if current object is directory ( d ), readable ( r ),cwritable ( w ) and executable ( x ).");
		description.add("Second column contains object size in bytes that is right aligned and occupies 10 characters.");
		description.add("Third column contains file creation date time.");
		description.add("Fourth and last column represents file name.");
		
		return description;
	}

}
