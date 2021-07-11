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
import hr.fer.zemris.java.model.PollOption;

@WebServlet("/servleti/glasanje-rezultati")
public class GlasanjeRezultati extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String pollIDString = (String) req.getParameter("pollID");
		Long pollID = null;
		
		try {
			pollID = Long.parseLong(pollIDString);
		}catch(Exception e) {
			List<String> messages = new ArrayList<>();
			messages.add("Coudn't find pollOption id.");
			req.setAttribute("messages",messages);
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		DAO dao = DAOProvider.getDao();
		List<PollOption> poolOptions = dao.getPollOptions(pollID);
		List<PollOption> best = dao.getHighestVotes(pollID);
		
		req.setAttribute("poolOptions",poolOptions);
		req.setAttribute("best",best);
		req.setAttribute("pollID", pollID);
		
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req,resp);
	}
}




























































