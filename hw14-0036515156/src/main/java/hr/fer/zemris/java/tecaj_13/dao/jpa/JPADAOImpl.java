package hr.fer.zemris.java.tecaj_13.dao.jpa;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOException;
import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		
		BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
		return blogEntry;
	}

	@Override
	public List<String> getUsernames() throws DAOException {
		
		List<String> usernames = JPAEMProvider.getEntityManager()
								.createQuery("SELECT user.nick FROM BlogUser as user",String.class)
								.getResultList();
		return usernames;
	}

	@Override
	public void insertUser(BlogUser user) throws DAOException {
		
		JPAEMProvider.getEntityManager().persist(user);
	}

	@Override
	public List<BlogUser> getRegisteredUsers() throws DAOException {
		
		List<BlogUser> users = JPAEMProvider.getEntityManager()
								.createQuery("SELECT user FROM BlogUser as user",BlogUser.class)
								.getResultList();
		return users;
	}

	@Override
	public BlogUser getUserByNick(String nick) throws DAOException {
		
		BlogUser user = null;
		
		try {
			user = JPAEMProvider.getEntityManager()
					.createQuery("SELECT user FROM BlogUser as user WHERE user.nick=:username",BlogUser.class)
					.setParameter("username",nick)
					.getSingleResult();
		}catch(NoResultException ignorable) {}
		return user;
	}

	@Override
	public void insertBlogEntry(BlogEntry entry) throws DAOException {
		
		JPAEMProvider.getEntityManager().persist(entry);
	}

	@Override
	public void insertBlogComment(BlogComment comment) throws DAOException {
		
		JPAEMProvider.getEntityManager().persist(comment);
	}
}



























































