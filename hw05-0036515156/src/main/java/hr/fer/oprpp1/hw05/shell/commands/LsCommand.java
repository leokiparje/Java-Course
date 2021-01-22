package hr.fer.oprpp1.hw05.shell.commands;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ParsePath;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * Ls command has its own description.
 * @author leokiparje
 *
 */

public class LsCommand implements ShellCommand{

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		
		if (arguments.length()<1) {
			env.writeln("Ls command takes a single argument but none were given.");
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
			env.writeln("Ls command takes a single argument but more were given.");
			env.write(env.getPromptSymbol()+" ");
			return ShellStatus.CONTINUE;
		}
		
		Path path = Paths.get(p);
		
		File directory = new File(path.toString());
		File[] children = directory.listFiles();
		
		for (File file : children) {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Path helperPath = Paths.get(file.toString());
			BasicFileAttributeView faView = Files.getFileAttributeView(helperPath, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
			BasicFileAttributes attributes = null;
			try{
				attributes = faView.readAttributes();
			}catch(Exception e) {
				System.out.println("Unable to read from file.");
			}
			FileTime fileTime = attributes.creationTime();
			String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
			
			StringBuilder sb = new StringBuilder();
			
			if (Files.isDirectory(helperPath)) sb.append("d");
			else sb.append("-");
			
			if (Files.isReadable(helperPath)) sb.append("r");
			else sb.append("-");
			
			if (Files.isWritable(helperPath)) sb.append("w");
			else sb.append("-");
			
			if (Files.exists(helperPath)) sb.append("x");
			else sb.append("-");
			
			System.out.println(String.format("%s %10d %s %s",sb.toString(), file.length(), formattedDateTime, file.getName()));
		}
		env.write(env.getPromptSymbol()+" ");
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "ls";
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
