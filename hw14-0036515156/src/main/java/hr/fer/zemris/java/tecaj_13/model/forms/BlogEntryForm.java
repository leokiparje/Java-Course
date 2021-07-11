package hr.fer.zemris.java.tecaj_13.model.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Class BlogEntryForm is used as a form for a blog entry.
 * @author leokiparje
 *
 */

public class BlogEntryForm {

	private String title;
	private String text;
	
	private Map<String,String> errors = new HashMap<>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
	
	public boolean hasError(String errorField) {
		if (errors.containsKey(errorField)) return true;
		return false;
	}
	
	public String getError(String errorField) {
		return errors.get(errorField);
	}
	
	public void setError(String error, String errorMessage) {
		errors.put(error,errorMessage);
	}
	
	public void validate() {
		if (this.title.isEmpty()) {
			errors.put("title","Title is required.");
		}
		if (this.text.isEmpty()) {
			errors.put("text","Text is required.");
		}
	}
	
	public void fillFormFromHttpRequest(HttpServletRequest req) {
		this.title = ifNullThenEmptyString(req.getParameter("title"));
		this.text = ifNullThenEmptyString(req.getParameter("text"));
	}
	
	public String ifNullThenEmptyString(String parameter) {
		if (parameter==null) return "";
		return parameter.trim();
	}
}
