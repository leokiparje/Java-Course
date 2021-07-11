package hr.fer.oprpp2.servlets;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class GlasanjeRezultati is http servlet that takes a request and adds attributes that the jsp file will use to render data.
 * @author leokiparje
 *
 */

@WebServlet("/glasanje-rezultati")
public class GlasanjeRezultati extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String definicija = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		
		List<Band> bandList = new ArrayList<>();
		
		Map<Integer,Integer> idToVotes = new HashMap<>();
		
		for (String line : getLines(definicija)) {
			int id;
			String name;
			String link;
			try {
				id = Integer.parseInt(line.split("\t")[0]);
				name = line.split("\t")[1].substring(0,line.split("\t")[1].lastIndexOf(" "));
				link = line.substring(line.lastIndexOf(" "));
				bandList.add(new Band(id,name,link));
				idToVotes.put(id, 0);
			}catch(Exception e) { System.out.println("Error while parsing band info."); }
		}
		
		for (String line : getLines(fileName)) {
			String[] parts = line.split(" ");
			int id = Integer.parseInt(parts[0]);
			int numOfVoted = Integer.parseInt(parts[1]);
			idToVotes.put(id,numOfVoted);
		}
		
		List<Band> bestBands = new ArrayList<>();
		
		int highestVote = -1;
		
		for (Band band : bandList) {
			if (idToVotes.get(band.getId())>highestVote) {
				bestBands.clear();
				highestVote = idToVotes.get(band.getId());
				bestBands.add(band);
			}else if(idToVotes.get(band.getId())==highestVote) {
				bestBands.add(band);
			}
		}
		
		req.setAttribute("idToVotes",idToVotes);
		req.setAttribute("bandList",bandList);
		req.setAttribute("bestBands",bestBands);
		
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req,resp);
	}
	
	private List<String> getLines(String fileName) throws IOException {
		
		List<String> lines = new ArrayList<>();
		File file = new File(fileName);
		Scanner sc = new Scanner(file);
		while(sc.hasNext()) {
			String line = sc.nextLine();
			lines.add(line);
		}
		sc.close();
		return lines;
	}
}
