package hr.fer.zemris.java.servlets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.dao.DAO;
import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.model.PollOption;

/**
 * Class Glasanje is a http servlet mapped to /glasanje. It shows glasanjeIndex.jsp page and passes band list which is obtained by 
 * reading from local file.
 * @author leokiparje
 *
 */

@WebServlet("/servleti/glasanje")
public class GlasanjeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String idString = req.getParameter("pollID");
		Long id = null;
		try {
			id = Long.parseLong(idString);
		}catch(Exception e) {
			List<String> messages = new ArrayList<>();
			messages.add("Coudn't find poll id.");
			req.setAttribute("messages",messages);
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		DAO dao = DAOProvider.getDao();
		
		List<PollOption> poolOptions = dao.getPollOptions(id);
		String title = dao.getTitle(id);
		String message = dao.getMessage(id);
		
		req.setAttribute("poolOptions",poolOptions);
		req.setAttribute("pollID",id);
		req.setAttribute("title",title);
		req.setAttribute("message",message);
		
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req,resp);
	}
}



