package hr.fer.oprpp2.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
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

/**
 * Class GlasanjeGrafika is http servlet used to draw a chart containing voting results.
 * @author leokiparje
 *
 */

@WebServlet("/glasanje-grafika")
public class GlasanjeGrafika extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String definicija = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		
		Map<Integer,String> idToName = new HashMap<>();
		
		for (String line : Files.readAllLines(Paths.get(definicija))) {
			int id;
			String name;
			try {
				id = Integer.parseInt(line.split("\t")[0]);
				name = line.split("\t")[1].substring(0,line.split("\t")[1].lastIndexOf(" "));
				idToName.put(id,name);
			}catch(Exception e) { System.out.println("Error while parsing band info."); }
		}
		
		Map<Integer,Integer> idToVotes = new HashMap<>();
		
		for (String line : Files.readAllLines(Paths.get(fileName))) {
			String[] parts = line.split(" ");
			int bandId = Integer.parseInt(parts[0]);
			int voteNum = Integer.parseInt(parts[1]);
			idToVotes.put(bandId,voteNum);
		}
		
		JFreeChart chart = getChart(idToVotes,idToName);
		
		resp.setContentType("image/png");
		resp.getOutputStream().write(ChartUtils.encodeAsPNG(chart.createBufferedImage(500,500)));
	}
	
	private JFreeChart getChart(Map<Integer,Integer> idToVotes, Map<Integer,String> idToName) {
		
		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
		
		for (Map.Entry<Integer,Integer> entry : idToVotes.entrySet()) {
			dataset.setValue(idToName.get(entry.getKey()),entry.getValue());
		}
		
		JFreeChart chart = ChartFactory.createPieChart("Votes",dataset,true,false,false);
		chart.setBorderVisible(false);
		return chart;
	}	
}
