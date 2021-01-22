package hr.fer.oprpp1.hw05.shell.commands;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import hr.fer.oprpp1.hw05.crypto.Util;
import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ParsePath;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * Hexdump command has its own description.
 * @author leokiparje
 *
 */

public class HexdumpCommand implements ShellCommand {

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
		
		if (pathLength < arguments.length()) {
			env.writeln("Hexdump command takes a single argument but more were given.");
			env.write(env.getPromptSymbol()+" ");
			return ShellStatus.CONTINUE;
		}
		
		Path path = Paths.get(p);
		
		try(InputStream is = Files.newInputStream(path, StandardOpenOption.READ)){
			
			byte[] buff = new byte[16];
			int index = 0x0;
			StringBuilder sb = new StringBuilder();
			byte[] oneByte = new byte[1];
			
			while(true) {
				try {
					int r = is.read(buff);
					if (r<1) break;	
					
					sb.append(String.format("%08x: ", index));
					for (int i=0;i<16;i++) {
						
						if (i==8) {
							sb.setLength(sb.length()-1);
							sb.append("|");
						}
						
						oneByte[0] = buff[i];
						
						if (i>=r) sb.append("   ");
						else sb.append(Util.bytetohex(oneByte)+" ");
						
					}
					
					sb.append("| ");
										
					for (int i=0;i<16;i++) {
						
						if (i>=r) {
							buff[i] = 32;
						}else {
							if (buff[i]<32 || buff[i]>127) {
								buff[i] = 46;
							}
						}
						
					}
					
					String line = new String(buff);
					
					sb.append(line);
					
					env.writeln(sb.toString());
					sb.setLength(0);
					index+=0x10;
					
				}catch(Exception e) {
					e.printStackTrace();
					env.writeln("Unable to read the buffer!");
					env.write(env.getPromptSymbol() + " ");
		            return ShellStatus.CONTINUE;
				}
			}
			
			
			
		}catch(Exception e) {
			System.out.println("Unable to read from file.");
			env.write(env.getPromptSymbol() + " ");
            return ShellStatus.CONTINUE;
		}
		
		env.write(env.getPromptSymbol()+" ");
		return ShellStatus.CONTINUE;
		
	}

	@Override
	public String getCommandName() {
		return "hexdump";
	}

	@Override
	public List<String> getCommandDescription() {
		
		List<String> description = new ArrayList<>();
		
		description.add("Hexdump command takes a single argument : file name.");
		description.add("It produces hex-output.");
		description.add("If user provides invalid or wrong argument for any of commands appropriate message is be written "
				+ "and shell is prepared to accept a new command from user.");
		
		return description;
		
	}
	
	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();
		sb.append("12345");
		int n = sb.length();
		for (int i=0;i<n;i++) {
			sb.append("lol");
		}
	}

}
