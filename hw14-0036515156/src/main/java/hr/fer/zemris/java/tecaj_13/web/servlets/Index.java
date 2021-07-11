package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class Index represents a servlet which is mapped empty url or /Index.jsp and is used to navigate user to the main page.
 * @author leokiparje
 *
 */

@WebServlet(urlPatterns={"/", "/Index.jsp"})
public class Index extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.sendRedirect(req.getServletContext().getContextPath() + "/servleti/main");
	}
}
