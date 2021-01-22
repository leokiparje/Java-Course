package hr.fer.oprpp1.hw05.shell;

import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;
import hr.fer.oprpp1.hw05.shell.commands.*;

/**
 * Class ShellEnvironment is a class representation of interface environment
 * @author leokiparje
 *
 */

public class ShellEnvironment implements Environment{
	
	private String PROMPT = ">";
	private String MORELINES  = "\\";
	private String MULTILINE = "|";
	
	Scanner sc = new Scanner(System.in);
	
	private SortedMap<String,ShellCommand> commands;
	
	/*
	 * Basic constructor
	 */
	
	public ShellEnvironment() {
		System.out.print("Welcome to MyShell v 1.0\n> ");
		initializeMap();
	}
	
	/*
	 * Method fills the map with elements
	 */
	
	public void initializeMap() {
		
		commands = new TreeMap<>();
		
		commands.put("cat", new CatCommand());
		commands.put("charsets", new CharsetsCommand());
		commands.put("copy", new CopyCommand());
		commands.put("hexdump", new HexdumpCommand());
		commands.put("ls", new LsCommand());
		commands.put("mkdir", new MkdirCommand());
		commands.put("tree", new TreeCommand());
		commands.put("help", new HelpCommand());
		commands.put("exit", new ExitCommand());
		commands.put("symbol", new SymbolCommand());
		
	}

	@Override
	public String readLine() throws ShellIOException {
		
		StringBuilder sb = new StringBuilder();
		
		String l = sc.nextLine();
			
		while(l.endsWith(MORELINES)) {
			
			sb.append(l, 0, l.length()-1);
			write(MULTILINE+" ");
			l = sc.nextLine();
		}
		
		sb.append(l);
		
		return sb.toString();
	}

	@Override
	public void write(String text) throws ShellIOException {
		System.out.print(text);		
	}

	@Override
	public void writeln(String text) throws ShellIOException {		
		System.out.println(text);		
	}

	@Override
	public SortedMap<String, ShellCommand> commands() {
		return commands;
	}

	@Override
	public Character getMultilineSymbol() {
		return MULTILINE.charAt(0);
	}

	@Override
	public void setMultilineSymbol(Character symbol) {
		MULTILINE = symbol.toString();
		
	}

	@Override
	public Character getPromptSymbol() {
		return PROMPT.charAt(0);
	}

	@Override
	public void setPromptSymbol(Character symbol) {
		PROMPT = symbol.toString();
	}

	@Override
	public Character getMorelinesSymbol() {
		return MORELINES.charAt(0);
	}

	@Override
	public void setMorelinesSymbol(Character symbol) {
		MORELINES = symbol.toString();
	}

	
	
}
