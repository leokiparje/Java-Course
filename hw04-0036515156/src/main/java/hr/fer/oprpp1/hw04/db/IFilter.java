package hr.fer.oprpp1.hw04.db;

public interface IFilter {
	
	boolean accepts(StudentRecord record);
	
}
