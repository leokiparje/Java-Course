package hr.fer.zemris.java.tecaj_13.model.forms;

import javax.servlet.http.HttpServletRequest;

/**
 * Class BlogCommentForm is used as a form for a blog comment.
 * @author leokiparje
 *
 */

public class BlogCommentForm {

	private String comment;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public boolean isEmpty() {
		return comment.isEmpty();
	}
	
	public String getErrorMessage() {
		return "Can't add empty comment.";
	}
	
	public void fillFormFromHttpRequest(HttpServletRequest req) {
		this.comment = ifNullThenEmptyString(req.getParameter("comment"));
	}
	
	public String ifNullThenEmptyString(String parameter) {
		if (parameter==null) return "";
		return parameter.trim();
	}
}
