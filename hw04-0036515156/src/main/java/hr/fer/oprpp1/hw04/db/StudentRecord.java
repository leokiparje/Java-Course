package hr.fer.oprpp1.hw04.db;

public class StudentRecord {
	
	private String jmbag;
	
	private String firstName;
	
	private String lastName;
	
	private int finalGrade;
	
	public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
		this.jmbag = jmbag;
		this.firstName = firstName;
		this.lastName = lastName;
		this.finalGrade = finalGrade;
	}
	
	public String getJmbag() {
		return jmbag;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public int getGrade() {
		return finalGrade;
	}
	
	public boolean equals(Object other) {
		
		if (other == this) return true;
		if (!(other instanceof StudentRecord)) return false;
		
		StudentRecord sr = (StudentRecord) other;
		
		return jmbag.equals(sr.jmbag);
		
	}
	
	public int hashCode() {
		
		return 29*jmbag.hashCode();
		
	}
	
}
