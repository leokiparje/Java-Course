package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.model.forms.LoginForm;

/**
 * Class Login represents a servlet which is mapped endpoint url ending with /servleti/login and has only one post method for login.
 * @author leokiparje
 *
 */

@WebServlet("/servleti/login")
public class Login extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		LoginForm form = new LoginForm();
		form.fillFormFromHttpRequest(req);
		form.validate();
		
		if (form.hasErrors()) {
			form.setPassword("");
			List<BlogUser> users = DAOProvider.getDAO().getRegisteredUsers();
			req.setAttribute("users",users);
			req.setAttribute("form",form);
			req.getRequestDispatcher("/WEB-INF/pages/Index.jsp").forward(req,resp);
			return;
		}
		
		BlogUser user = DAOProvider.getDAO().getUserByNick(form.getNick());
		
		if (user==null) {
			form.setPassword("");
			form.setError("nick","Username not found.");
			req.setAttribute("form",form);
			List<BlogUser> users = DAOProvider.getDAO().getRegisteredUsers();
			req.setAttribute("users",users);
			req.getRequestDispatcher("/WEB-INF/pages/Index.jsp").forward(req,resp);
			return;
		}else if(!user.getPasswordHash().equals(form.getPasswordHash())) {
			form.setPassword("");
			form.setError("password","Incorrect password.");
			req.setAttribute("form",form);
			List<BlogUser> users = DAOProvider.getDAO().getRegisteredUsers();
			req.setAttribute("users",users);
			req.getRequestDispatcher("/WEB-INF/pages/Index.jsp").forward(req,resp);
			return;
		}
		try { addSession(req,resp,user); }
		catch(Exception e) {}
	}
	
	public void addSession(HttpServletRequest req, HttpServletResponse resp, BlogUser user) throws IOException {
		req.getSession().setAttribute("user",user);
		resp.sendRedirect(req.getServletContext().getContextPath() + "/servleti/main");
	}
}
