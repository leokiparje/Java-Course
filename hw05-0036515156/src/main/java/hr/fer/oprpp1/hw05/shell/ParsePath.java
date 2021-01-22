package hr.fer.oprpp1.hw05.shell;

/**
 * Class ParsePath has a static method that parses given arguments
 * @author leokiparje
 *
 */

public class ParsePath {

	/*
	 * Method parse takes a String text and parses it by allowing escaping if String starts with double quotes
	 */
	
	public static String parse(String text) {
		
		StringBuilder sb = new StringBuilder();
		
		if (text.startsWith("\"")){
			
			int index = 1;
			
			while (index<text.length()) {
				
				if (text.charAt(index)=='\\') {		
					index++;
					if (index==text.length()) throw new ShellIOException("Wrong input of file path.");
					if (text.charAt(index)=='\\' || text.charAt(index)=='\"') {
						sb.append(text.charAt(index++));
					}else {
						throw new ShellIOException("Wrong input of file path.");
					}
					
				}else if(text.charAt(index)=='\"') {
					index++;
					break;
				}else {
					sb.append(text.charAt(index++));
				}
				
			}
			
			if (index==text.length() || text.charAt(index)==' ') {
				return sb.toString();
			}
			
			throw new ShellIOException("Wrong input of file path.");
			
		}
			
		if (text.contains(" ")) {
            return text.substring(0, text.indexOf(" "));
        } else {
            return text;
        }
		
	}
	
}
