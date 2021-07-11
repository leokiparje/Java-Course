package hr.fer.oprpp2.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ColorSetter is a http servlet used to map /setcolor/* to colors.jsp, where * is a wildcard representing any string(empty inclusive).
 * @author leokiparje
 *
 */

@WebServlet("/setcolor/*")
public class ColorSetter extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String bgColor = req.getPathInfo().substring(1);

		if (bgColor!=null) {
			req.getSession().setAttribute("pickedBgCol",bgColor);
		}
		
		req.getRequestDispatcher("/WEB-INF/pages/colors.jsp").forward(req,resp);
	}
}
