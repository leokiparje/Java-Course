package hr.fer.zemris.java.tecaj_13.model.forms;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Class BlogUserForm is used as a form for a blog user.
 * @author leokiparje
 *
 */

public class BlogUserForm {

	private String id;
	private String firstName;
	private String lastName;
	private String nick;
	private String email;
	private String password;
	
	private Map<String, String> userFormErrors = new HashMap<>();
	
	public BlogUserForm() {}
	
	public String getError(String errorFieldName) {
		return userFormErrors.get(errorFieldName);
	}
	
	public boolean hasErrors() {
		return !userFormErrors.isEmpty();
	}
	
	public boolean hasError(String errorFieldName) {
		return userFormErrors.containsKey(errorFieldName);
	}
	
	public void fillFormFromHttpRequest(HttpServletRequest req) {
		this.id = ifNullThenEmptyString(req.getParameter("id"));
		this.firstName = ifNullThenEmptyString(req.getParameter("ime"));
		this.lastName = ifNullThenEmptyString(req.getParameter("prezime"));
		this.nick = ifNullThenEmptyString(req.getParameter("nick"));
		this.email = ifNullThenEmptyString(req.getParameter("email"));
		this.password = ifNullThenEmptyString(req.getParameter("password"));
	}
	
	public void fillFormFromUser(BlogUser user) {
		
		if (user.getId()==null) this.id = "";
		else this.id = user.getId().toString();
		
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.nick = user.getNick();
		this.email = user.getEmail();
	}
	
	public String ifNullThenEmptyString(String parameter) {
		if (parameter==null) return "";
		return parameter.trim();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Map<String, String> getUserFormErrors() {
		return userFormErrors;
	}

	public void setUserFormErrors(Map<String, String> userFormErrors) {
		this.userFormErrors = userFormErrors;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void validate() {
		userFormErrors.clear();
		
		if(!this.id.isEmpty()) {
			try {
				Long.parseLong(this.id);
			} catch(NumberFormatException ex) {
				userFormErrors.put("id", "Vrijednost identifikatora nije valjana.");
			}
		}
		
		if(this.firstName.isEmpty()) {
			userFormErrors.put("ime", "Ime je obavezno!");
		}
		
		if(this.lastName.isEmpty()) {
			userFormErrors.put("prezime", "Prezime je obavezno!");
		}

		if(this.email.isEmpty()) {
			userFormErrors.put("email", "EMail je obavezan!");
		} else {
			int l = email.length();
			int p = email.indexOf('@');
			if(l<3 || p==-1 || p==0 || p==l-1) {
				userFormErrors.put("email", "EMail nije ispravnog formata.");
			}
		}
		if (this.password.length()<5) {
			userFormErrors.put("password","Password mora imati minimalno 5 charactera.");
		}
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
























































