package hr.fer.zemris.java.tecaj_13.dao;

import java.util.List;
import java.util.Set;

import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

public interface DAO {

	/**
	 * Dohvaća entry sa zadanim <code>id</code>-em. Ako takav entry ne postoji,
	 * vraća <code>null</code>.
	 * 
	 * @param id ključ zapisa
	 * @return entry ili <code>null</code> ako entry ne postoji
	 * @throws DAOException ako dođe do pogreške pri dohvatu podataka
	 */
	BlogEntry getBlogEntry(Long id) throws DAOException;
	
	List<String> getUsernames() throws DAOException;
	
	void insertUser(BlogUser user) throws DAOException;
	
	void insertBlogEntry(BlogEntry entry) throws DAOException;
	
	void insertBlogComment(BlogComment comment) throws DAOException;
	
	List<BlogUser> getRegisteredUsers() throws DAOException;
	
	BlogUser getUserByNick(String nick) throws DAOException;
}