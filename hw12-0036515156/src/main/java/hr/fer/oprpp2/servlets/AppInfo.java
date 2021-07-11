package hr.fer.oprpp2.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AppInfo Servlet je mapiran na appInfo stazu i korisniku prikazuje appInfo.jsp
 * @author leokiparje
 *
 */

@WebServlet("/appInfo")
public class AppInfo extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.getRequestDispatcher("/WEB-INF/pages/appInfo.jsp").forward(req, resp);
	}
}
