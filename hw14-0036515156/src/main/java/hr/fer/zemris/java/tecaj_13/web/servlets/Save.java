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
import hr.fer.zemris.java.tecaj_13.model.forms.BlogUserForm;

/**
 * Class Save represents a servlet which is mapped to an endpoint /servleti/save which processes the registration of a new user.
 * @author leokiparje
 *
 */

@WebServlet("/servleti/save")
public class Save extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		obradi(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		obradi(req, resp);
	}
	
	protected void obradi(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		
		String metoda = req.getParameter("metoda");
		if(!"Pohrani".equals(metoda)) {
			resp.sendRedirect(req.getServletContext().getContextPath() + "/servleti/main");
			return;
		}

		BlogUserForm userForm = new BlogUserForm();
		userForm.fillFormFromHttpRequest(req);
		userForm.validate();
		
		List<String> usernames = DAOProvider.getDAO().getUsernames();
		
		if(userForm.hasErrors() || usernames.contains(userForm.getNick())) {
			
			if (usernames.contains(userForm.getNick())) {
				userForm.getUserFormErrors().put("nick","Username already exists.");
			}
			
			req.setAttribute("userForm",userForm);
			req.getRequestDispatcher("/WEB-INF/pages/Registracija.jsp").forward(req, resp);
			return;
		}
		
		BlogUser user = new BlogUser();
		user.fillAtributesFromForm(userForm);
		user.setPasswordHash(userForm.getPasswordHash());
		
		DAOProvider.getDAO().insertUser(user); 

		resp.sendRedirect(req.getServletContext().getContextPath() + "/servleti/main");
	}
}
