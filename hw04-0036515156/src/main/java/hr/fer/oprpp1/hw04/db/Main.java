package hr.fer.oprpp1.hw04.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
	
	public static void main(String[] args) {

        List<String> lines = null;

        try {
            lines = Files.readAllLines(
                    Paths.get("database.txt"),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            System.out.println("Nemoguće pročitati datoteku!");
            System.exit(1);
        }
        
        StudentDatabase database = new StudentDatabase(lines);
        Scanner sc = new Scanner(System.in);
        String line;
        
        while(true) {
        	
        	System.out.print("> ");       	
        	line = sc.nextLine();
        	
        	if (line.equals("exit")) {
        		System.out.println("Goodbye!");
        		break;
        	}
        	if (line.length()==0) continue;
        	if (!line.substring(0, 5).equals("query") || line.length()==5) {
        		System.out.println("Wrong query format.");
        		continue;
        	}
        	
        	
        	QueryParser parsed = new QueryParser(line.substring(5));
        	List<StudentRecord> listStudents = new ArrayList<>();
        	List<StudentRecord> pomocni = new ArrayList<>();
        	
        	if (parsed.isDirectQuery()) {
        		System.out.println("Using index for record retrieval.");
        		listStudents.add(database.forJMBAG((parsed.getQueriedJmbag())));
        	}else {
        		List<List<ConditionalExpression>> list = parsed.getQuery();
        		for (List<ConditionalExpression> l : list) {
        			
        			List<StudentRecord> listsr = database.filter(new QueryFilter(l));
        			
        			for (StudentRecord sr : listsr) {
        				listStudents.add(sr);
        			}
        		}
        	}    	
        	
        	/*
        	else if (parsed.isOrQuery() || parsed.isSimpleQuery()) {
        		listStudents = database.filter(new QueryFilterOR(parsed.getQuery()));
        	}else if (parsed.isAndQuery()) {
        		listStudents = database.filter(new QueryFilter(parsed.getQuery()));
        	}else {
        		listStudents = database.filter(new QueryFilter(parsed.getQuery())); 
        	}
        	*/
        	
        	if (listStudents.size()>0) {
        		System.out.println(output(listStudents));
        	}
        	System.out.println("Records selected: " + listStudents.size() + "\n");
        }
        
        sc.close();
	}
        
	public static String output(List<StudentRecord> listStudents) {
		
		int longestJmbag=0;
		int longestLastName=0;
		int longestFirstName=0;
		
		for (StudentRecord sr : listStudents) {
			if (sr.getFirstName().length()>longestFirstName) longestFirstName = sr.getFirstName().length();
			if (sr.getLastName().length()>longestLastName) longestLastName = sr.getLastName().length();
			if (sr.getJmbag().length()>longestJmbag) longestJmbag = sr.getJmbag().length();
		}
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("+");
		for (int i=0;i<longestJmbag+2;i++) { sb.append("="); }
		sb.append("+");
		for (int i=0;i<longestLastName+2;i++) { sb.append("="); }
		sb.append("+");
		for (int i=0;i<longestFirstName+2;i++) { sb.append("="); }
		sb.append("+");
		sb.append("=");
		sb.append("=");
		sb.append("=");
		sb.append("+");
		sb.append("\n");
		
		String block = sb.toString();
		
		for (StudentRecord sr : listStudents) {
			String[] s = {sr.getJmbag(), sr.getLastName(), sr.getFirstName(), String.valueOf(sr.getGrade())};
			sb.append("| ");
			sb.append(s[0]);
			for (int i=0;i<longestJmbag-s[0].length();i++) {sb.append(" ");}
			sb.append(" | ");
			sb.append(s[1]);
			for (int i=0;i<longestLastName-s[1].length();i++) {sb.append(" ");}
			sb.append(" | ");
			sb.append(s[2]);
			for (int i=0;i<longestFirstName-s[2].length();i++) {sb.append(" ");}
			sb.append(" | ");
			sb.append(s[3]);
			sb.append(" |");
			sb.append("\n");
		}
		
		sb.append(block);
		sb.setLength(sb.length()-1);
		
		return sb.toString();
		
	}
	
}
