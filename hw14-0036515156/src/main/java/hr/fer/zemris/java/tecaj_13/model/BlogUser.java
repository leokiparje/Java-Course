package hr.fer.zemris.java.tecaj_13.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import hr.fer.zemris.java.tecaj_13.model.forms.BlogUserForm;

/**
* Class BlogUser is a model class which is mapped to the table in blog database of the same name
* @author leokiparje
*
*/

@Entity
@Table(name="blog_users")
public class BlogUser {

	@Id @GeneratedValue
	private Long id;
	@Column(length=100,nullable=false)
	private String firstName;
	@Column(length=100,nullable=false)
	private String lastName;
	@Column(length=100)
	private String nick;
	@Column(length=100,nullable=false,unique=true)
	private String email;
	@Column(length=100,nullable=false)
	private String passwordHash;
	@OneToMany(mappedBy="author",fetch=FetchType.LAZY,cascade=CascadeType.PERSIST,orphanRemoval=true)
	private List<BlogEntry> entries;
	
	public BlogUser() {}
	
	public List<BlogEntry> getEntries(){
		return this.entries;
	}
	
	public void setEntries(List<BlogEntry> entries) {
		this.entries = entries;
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
	public Long getId() {
		return id;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String hash) {
		this.passwordHash = hash;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogUser other = (BlogUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public void fillAtributesFromForm(BlogUserForm form) {
		this.firstName = form.getFirstName();
		this.lastName = form.getLastName();
		this.nick = form.getNick();
		this.email = form.getEmail();
	}
}
