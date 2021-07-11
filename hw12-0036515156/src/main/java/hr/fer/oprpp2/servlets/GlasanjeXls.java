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

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Class GlasanjeXls is a http servlet which take a GET request and dowloads excel file of voting results as a respone.
 * @author leokiparje
 *
 */

@WebServlet("/glasanje-xls")
public class GlasanjeXls extends HttpServlet {

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
		
		resp.setContentType("application/octet-stream");
		
		@SuppressWarnings("resource")
		Workbook hwb=new HSSFWorkbook();
		
		HSSFSheet sheet =  (HSSFSheet) hwb.createSheet("Results of voting");
		HSSFRow names = sheet.createRow((short)1);
		HSSFRow votes = sheet.createRow((short)2);
		int cellNum = 0;
		for (Map.Entry<Integer,String> entry : idToName.entrySet()) {
			names.createCell((short)cellNum).setCellValue(entry.getValue());
			votes.createCell((short)cellNum++).setCellValue(idToVotes.get(entry.getKey())==null ? 0 : idToVotes.get(entry.getKey()));
		}
		
		resp.setHeader("content-Disposition","attachment; filename=\"tablica.xls\"");
		hwb.write(resp.getOutputStream());
	}
}
