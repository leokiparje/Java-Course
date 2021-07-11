package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.model.forms.LoginForm;

/**
 * Class Main represents a servlet which is mapped to any main endpoint /servleti/main and renders main page of this application.
 * @author leokiparje
 *
 */

@WebServlet("/servleti/main")
public class Main extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		BlogUser user = new BlogUser();
		LoginForm loginForm= new LoginForm();
		loginForm.fillFormFromUser(user);
		
		List<BlogUser> users = DAOProvider.getDAO().getRegisteredUsers();
		
		req.setAttribute("users",users);
		req.setAttribute("userForm",loginForm);
		
		req.getRequestDispatcher("/WEB-INF/pages/Index.jsp").forward(req, resp);
	}
}
