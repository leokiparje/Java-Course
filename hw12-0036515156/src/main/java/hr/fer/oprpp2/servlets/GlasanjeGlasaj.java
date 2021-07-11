package hr.fer.oprpp2.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
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

/**
 * Class GlasanjeGlasaj is a http servlet which is called when a client votes for a band.
 * Request is passed to this servlet and vote counter for that specific band is incremented.
 * @author leokiparje
 *
 */

@WebServlet("/glasanje-glasaj")
public class GlasanjeGlasaj extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		
		String id = req.getParameter("id");
		
		if (id==null) {
			List<String> messages = new ArrayList<>();
			messages.add("Coudn't find id of a voting band.");
			req.setAttribute("messages",messages);
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		Map<Integer,Integer> idToVotes = new HashMap<>();
		
		for (String line : Files.readAllLines(Paths.get(fileName))) {
			String[] parts = line.split(" ");
			int bandId = Integer.parseInt(parts[0]);
			int voteNum = Integer.parseInt(parts[1]);
			if (parts[0].equals(id)) voteNum++;
			idToVotes.put(bandId,voteNum);
		}
		
		StringBuilder sb = new StringBuilder();
		OutputStream os = Files.newOutputStream(Paths.get(fileName));
		
		for (Map.Entry<Integer,Integer> entry : idToVotes.entrySet()) {
			sb.append(entry.getKey()).append(" ").append(entry.getValue()).append("\n");
		}
		sb.setLength(sb.length()-1);
		os.write(sb.toString().getBytes(StandardCharsets.UTF_8));
		
		os.flush();
		os.close();
		
		resp.sendRedirect(req.getContextPath()+"/glasanje-rezultati");
	}
}






































































