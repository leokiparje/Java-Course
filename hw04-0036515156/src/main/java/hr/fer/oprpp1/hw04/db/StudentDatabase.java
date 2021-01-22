package hr.fer.oprpp1.hw04.db;

import java.util.*;

/**
 * Class StudentBase represents databse of StudentRecords
 * @author leokiparje
 *
 */

public class StudentDatabase {
	
	/*
	 * inner list of StudentRecords
	 */
	
	List<StudentRecord> record;	
	
	/*
	 * inner map with key value jmbag of StudentRecord and value is that StudentRecord
	 */
	
	Map<String, StudentRecord> index;
	
	/*
	 * basic constructor
	 */
	
	public StudentDatabase(List<String> lines) {
		
		record = new ArrayList<>();	
		index = new HashMap<>();		
		parse(lines);
		
	}
	
	/*
	 * Method parses list of Strings
	 */
	
	public void parse(List<String> lines) {
		
		Set<String> jmbags = new HashSet<>();
		
		for (String s : lines) {
			
			String[] strings = s.split("\t");
			if (strings.length!=4) throw new RuntimeException("Illegal record format!");
			try {
				String jmbag = strings[0];
				int grade = Integer.parseInt(strings[3]);
				if (grade<1 || grade>5) throw new IllegalArgumentException("Illegal grade in record!");
				if (!jmbags.add(jmbag)) throw new IllegalArgumentException("Duplicate jmbag in record!");
				StudentRecord newStudent = new StudentRecord(jmbag, strings[1], strings[2], grade);
				record.add(newStudent);
				index.put(jmbag, newStudent);
			}catch(IllegalArgumentException e) {
				System.out.println(e);
			}catch(RuntimeException e) {
				throw new RuntimeException("Parsing exception!");
			}
			
		}
		
	}
	
	/*
	 * Method returns StudentRecord for given jmbag in O(1)
	 */
	
	public StudentRecord forJMBAG(String jmbag) {
		StudentRecord sr = index.get(jmbag);
		if (sr==null) return null;
		return sr;
	}
	
	/*
	 * Method returns list of StudentRecords filtered by filter IFilter
	 */
	
	public List<StudentRecord> filter(IFilter filter){
		List<StudentRecord> temporary = new ArrayList<>();
		for (StudentRecord sr : record) {
			if (filter.accepts(sr)) temporary.add(sr);
		}
		return temporary;
	}
	
}
