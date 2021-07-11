package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.model.forms.BlogCommentForm;
import hr.fer.zemris.java.tecaj_13.model.forms.BlogEntryForm;

/**
 * Class Author represents a servlet which is mapped to any endpoint ending with /servleti/author
 * @author leokiparje
 *
 */

@WebServlet("/servleti/author/*")
public class Author extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String path = req.getPathInfo();
		if (path.endsWith("new")) {
			try { newBlogEntry(req,resp,path); }
			catch(Exception e) {e.printStackTrace();}
			return;
		}
		if (path.substring(path.lastIndexOf("/")+1).startsWith("edit")) {
			try { editBlogEntry(req,resp,path); }
			catch(Exception e) {e.printStackTrace();}
			return;
		}
		String eid = path.substring(path.lastIndexOf("/")+1);
		try {
			Long id = Long.parseLong(eid);
			blogEntry(req,resp,path,id);
			return;
		}catch(Exception e) {e.printStackTrace();}
		
		String nick = req.getPathInfo().substring(req.getPathInfo().lastIndexOf("/")+1);
		
		BlogUser author = DAOProvider.getDAO().getUserByNick(nick);
		BlogUser user = (BlogUser)req.getSession().getAttribute("user");
		
		req.setAttribute("author",author);
		req.setAttribute("a",nick);
		req.setAttribute("user",user);
		
		req.getRequestDispatcher("/WEB-INF/pages/Author.jsp").forward(req, resp);
	}
	
	private void blogEntry(HttpServletRequest req, HttpServletResponse resp, String path, Long id) throws Exception {
		
		path = path.substring(0,path.lastIndexOf("/"));
		String nick = path.substring(path.lastIndexOf("/")+1);
		
		BlogUser author = DAOProvider.getDAO().getUserByNick(nick);
		BlogUser user = (BlogUser)req.getSession().getAttribute("user");
		BlogEntry entry = DAOProvider.getDAO().getBlogEntry(id);
		
		req.setAttribute("author",author);
		req.setAttribute("user",user);
		req.setAttribute("entry",entry);
		
		req.getRequestDispatcher("/WEB-INF/pages/ShowBlogEntry.jsp").forward(req, resp);
	}
	
	private void editBlogEntry(HttpServletRequest req, HttpServletResponse resp, String path) throws Exception {
		
		path = path.substring(0,path.lastIndexOf("/"));
		String nick = path.substring(path.lastIndexOf("/")+1);
		Long id = Long.parseLong(req.getParameter("id"));
		
		BlogUser author = DAOProvider.getDAO().getUserByNick(nick);
		BlogUser user = (BlogUser)req.getSession().getAttribute("user");
		BlogEntry entry = DAOProvider.getDAO().getBlogEntry(Long.parseLong(req.getParameter("id")));
		BlogEntryForm form = new BlogEntryForm();
		form.setTitle(entry.getTitle());
		form.setText(entry.getText());
		form.validate();
		
		req.setAttribute("author",author);
		req.setAttribute("user",user);
		req.setAttribute("form",form);
		req.setAttribute("id",id);
		
		req.getRequestDispatcher("/WEB-INF/pages/EditBlogEntry.jsp").forward(req, resp);
	}
	
	private void newBlogEntry(HttpServletRequest req, HttpServletResponse resp, String path) throws Exception {
		
		path = path.substring(0,path.lastIndexOf("/"));
		String nick = path.substring(path.lastIndexOf("/")+1);
		
		BlogUser author = DAOProvider.getDAO().getUserByNick(nick);
		BlogUser user = (BlogUser)req.getSession().getAttribute("user");
		
		req.setAttribute("author",author);
		req.setAttribute("user",user);
		
		req.getRequestDispatcher("/WEB-INF/pages/NewBlogEntry.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");

		String path = req.getPathInfo();
		
		if (path.endsWith("new")) {
			try { addNewEntry(req,resp); }
			catch(Exception e) {e.printStackTrace();}
			return;
		}
		if (path.substring(path.lastIndexOf("/")+1).startsWith("newComment")) {
			Long eid = Long.parseLong(req.getParameter("eid"));
			try { addNewComment(req,resp,eid); }
			catch(Exception e) {e.printStackTrace();}
			return;
		}
		
		// EDIT
		
		BlogEntryForm form = new BlogEntryForm();
		form.fillFormFromHttpRequest(req);
		form.validate();
		
		Long id = Long.parseLong(req.getParameter("id"));
		
		if(form.hasErrors()) {
			
			req.setAttribute("form",form);
			req.setAttribute("user",req.getSession().getAttribute("user"));
			BlogEntry entry = DAOProvider.getDAO().getBlogEntry(Long.parseLong(req.getParameter("id")));
			req.setAttribute("author",entry.getAuthor());
			req.setAttribute("id",Long.parseLong(req.getParameter("id")));
			req.getRequestDispatcher("/WEB-INF/pages/EditBlogEntry.jsp").forward(req, resp);
			return;
		}
		
		BlogEntry entry = DAOProvider.getDAO().getBlogEntry(id);
		
		Date currentTime = new Date();
		
		entry.setTitle(form.getTitle());
		entry.setText(form.getText());
		entry.setLastModifiedAt(currentTime);
		
		DAOProvider.getDAO().insertBlogEntry(entry);

		resp.sendRedirect(req.getServletContext().getContextPath() + "/servleti/author/"+entry.getAuthor().getNick());
	}
	
	private void addNewComment(HttpServletRequest req, HttpServletResponse resp, Long eid) throws Exception {
		
		//if (req.getParameter("comment").length()>5) req.getRequestDispatcher("/WEB-INF/pages/Index.jsp").forward(req, resp);
		
		BlogCommentForm form = new BlogCommentForm();
		form.fillFormFromHttpRequest(req);
		
		BlogEntry entry = DAOProvider.getDAO().getBlogEntry(Long.parseLong(req.getParameter("eid")));
		
		if (form.isEmpty()) {
			req.setAttribute("form",form);
			req.setAttribute("entry",entry);
			req.setAttribute("author",entry.getAuthor());
			req.getRequestDispatcher("/WEB-INF/pages/ShowBlogEntry.jsp").forward(req, resp);
			return;
		}
		
		BlogComment comment = new BlogComment();
		
		BlogUser user = (BlogUser)req.getSession().getAttribute("user");
		Date currentTime = new Date();
		
		comment.setBlogEntry(entry);
		if (user==null) {
			comment.setUsersEMail("anonymus");
		}else {
			comment.setUsersEMail(user.getEmail());
		}
		comment.setMessage(form.getComment());
		comment.setPostedOn(currentTime);
		
		DAOProvider.getDAO().insertBlogComment(comment);
		
		resp.sendRedirect(req.getServletContext().getContextPath() + "/servleti/author/"+entry.getAuthor().getNick()+"/"+entry.getId());
	}
	
	private void addNewEntry(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		BlogEntryForm form = new BlogEntryForm();
		form.fillFormFromHttpRequest(req);
		form.validate();
		
		if(form.hasErrors()) {
			
			req.setAttribute("form",form);
			req.getRequestDispatcher("/WEB-INF/pages/NewBlogEntry.jsp").forward(req, resp);
			return;
		}
		
		BlogUser author = (BlogUser)req.getSession().getAttribute("user");
		Date currentTime = new Date();
		
		BlogEntry entry = new BlogEntry();
		
		entry.setTitle(form.getTitle());
		entry.setText(form.getText());
		entry.setAuthor(author);
		entry.setCreatedAt(currentTime);
		entry.setLastModifiedAt(currentTime);
		
		DAOProvider.getDAO().insertBlogEntry(entry);

		resp.sendRedirect(req.getServletContext().getContextPath() + "/servleti/author/"+author.getNick());
	}
}
