package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import hr.fer.zemris.java.dao.DAO;
import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.model.PollOption;

/**
 * Class GlasanjeGrafika is http servlet used to draw a chart containing voting results.
 * @author leokiparje
 *
 */

@WebServlet("/servleti/glasanje-grafika")
public class GlasanjeGrafika extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String pollIDString = req.getParameter("pollID");
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
		
		List<PollOption> pollOptions = dao.getPollOptions(pollID);
		
		JFreeChart chart = getChart(pollOptions);
		
		resp.setContentType("image/png");
		resp.getOutputStream().write(ChartUtils.encodeAsPNG(chart.createBufferedImage(500,500)));
	}
	
	private JFreeChart getChart(List<PollOption> pollOptions) {
		
		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
		
		for (PollOption pollOption : pollOptions) {
			int votes = pollOption.getVotesCount();
			String title = pollOption.getOptionTitle();
			dataset.setValue(title,votes);
		}
		
		JFreeChart chart = ChartFactory.createPieChart("Votes",dataset,true,false,false);
		chart.setBorderVisible(false);
		return chart;
	}	
}
