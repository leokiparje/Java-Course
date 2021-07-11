package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.dao.DAO;
import hr.fer.zemris.java.dao.DAOProvider;

@WebServlet("/servleti/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String pollIDString = req.getParameter("pollID");
		String idString = req.getParameter("pollOptionID");
		Long pollID = null;
		Long optionId = null;
		try {
			pollID = Long.parseLong(pollIDString);
			optionId = Long.parseLong(idString);
		}catch(Exception e) {
			List<String> messages = new ArrayList<>();
			messages.add("Coudn't find pollOption id.");
			req.setAttribute("messages",messages);
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		DAO dao = DAOProvider.getDao();
		dao.incrementVote(optionId);
		
		resp.sendRedirect(req.getContextPath()+"/servleti/glasanje-rezultati?pollID="+pollID);
	}
}
