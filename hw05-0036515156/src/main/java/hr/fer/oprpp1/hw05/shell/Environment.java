package hr.fer.oprpp1.hw05.shell;

import java.util.SortedMap;

/**
 * Interface Environment has 10 usefull methods
 * 
 * @author leokiparje
 *
 */

public interface Environment {
	
	String readLine() throws ShellIOException;
	void write(String text) throws ShellIOException;
	void writeln(String text) throws ShellIOException;
	SortedMap<String, ShellCommand> commands();
	Character getMultilineSymbol();
	void setMultilineSymbol(Character symbol);
	Character getPromptSymbol();
	void setPromptSymbol(Character symbol);
	Character getMorelinesSymbol();
	void setMorelinesSymbol(Character symbol);
	
}
