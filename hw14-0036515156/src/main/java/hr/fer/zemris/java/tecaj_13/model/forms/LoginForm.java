package hr.fer.zemris.java.tecaj_13.model.forms;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Class LoginForm is used as a form when logging in the application.
 * @author leokiparje
 *
 */

public class LoginForm {

	private String nick;
	private String password;
	
	private Map<String,String> errors = new HashMap<>();
	
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void fillFormFromUser(BlogUser user) {
		this.nick = user.getNick();
	}
	
	public void fillFormFromHttpRequest(HttpServletRequest req) {
		this.nick = ifNullThenEmptyString(req.getParameter("nick"));
		this.password = ifNullThenEmptyString(req.getParameter("password"));
	}
	
	public String ifNullThenEmptyString(String parameter) {
		if (parameter==null) return "";
		return parameter.trim();
	}
	
	public void setError(String error, String errorMessage) {
		errors.put(error,errorMessage);
	}
	
	public void validate() {
		if (this.nick.isEmpty()) {
			errors.put("nick","Username required.");
		}
		if (this.password.isEmpty()) {
			errors.put("password","Password is required.");
		}
	}
	
	public boolean hasError(String error) {
		if (errors.containsKey(error)) return true;
		return false;
	}
	
	public String getError(String error) {
		return errors.get(error);
	}
	
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
	
	public String getHash() throws Exception {
		byte[] buff = password.getBytes();
		
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		
		digest.update(buff,0,buff.length);
		byte[] digested = digest.digest();
		
		StringBuilder hash = new StringBuilder();
		
		for (Byte b : digested) hash.append(String.format("%02x",b));
		
		return hash.toString();
	}
	
	public String getPasswordHash() {
		
		String hash = null;
		
		try {
			hash = getHash();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return hash;
	}
}
