package hr.fer.oprpp2.servlets;

/**
 * Class Band represents one band with band id, band name and link to one song of the band.
 * @author leokiparje
 *
 */

public class Band {

	private int id;
	private String name;
	private String link;
	
	public Band(int id, String name, String link) {
		this.id = id;
		this.name = name;
		this.link = link;
	}
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getLink() {
		return link;
	}
}
