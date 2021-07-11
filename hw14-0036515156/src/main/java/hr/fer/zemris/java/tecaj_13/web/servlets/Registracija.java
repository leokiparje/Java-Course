package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.model.forms.BlogUserForm;

/**
 * Class Registracija represents a servlet which is mapped to an endpoint ending with /servleti/registracija and renders
 * registration page to user.
 * @author leokiparje
 *
 */

@WebServlet("/servleti/registracija")
public class Registracija extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		BlogUser user = new BlogUser();
		BlogUserForm userForm = new BlogUserForm();
		userForm.fillFormFromUser(user);
		
		req.setAttribute("userForm",userForm);
		
		req.getRequestDispatcher("/WEB-INF/pages/Registracija.jsp").forward(req, resp);
	}
	
}
