package hr.fer.oprpp2.servlets;

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

/**
 * Class Glasanje is a http servlet mapped to /glasanje. It shows glasanjeIndex.jsp page and passes band list which is obtained by 
 * reading from local file.
 * @author leokiparje
 *
 */

@WebServlet("/glasanje")
public class Glasanje extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		
		List<Band> bandList = new ArrayList<>();
		
		for (String line : getLines(fileName)) {
			int id;
			String name;
			String link;
			try {
				id = Integer.parseInt(line.split("\t")[0]);
				name = line.split("\t")[1].substring(0,line.split("\t")[1].lastIndexOf(" "));
				link = line.substring(line.lastIndexOf(" "));
				bandList.add(new Band(id,name,link));
			}catch(Exception e) { System.out.println("Error while parsing band info."); }
		}
		
		req.setAttribute("bandList",bandList);
		
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req,resp);
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











































































