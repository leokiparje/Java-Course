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

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import hr.fer.zemris.java.dao.DAO;
import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.model.PollOption;

/**
 * Class GlasanjeXls is a http servlet which take a GET request and dowloads excel file of voting results as a respone.
 * @author leokiparje
 *
 */

@WebServlet("/servleti/glasanje-xls")
public class GlasanjeXls extends HttpServlet {

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
		
		resp.setContentType("application/octet-stream");
		
		@SuppressWarnings("resource")
		Workbook hwb=new HSSFWorkbook();
		
		HSSFSheet sheet =  (HSSFSheet) hwb.createSheet("Results of voting");
		HSSFRow names = sheet.createRow((short)1);
		HSSFRow votes = sheet.createRow((short)2);
		int cellNum = 0;
		
		for (PollOption pollOption : pollOptions) {
			names.createCell((short)cellNum).setCellValue(pollOption.getOptionTitle());
			votes.createCell((short)cellNum++).setCellValue(pollOption.getVotesCount());
		}
		
		resp.setHeader("content-Disposition","attachment; filename=\"tablica.xls\"");
		hwb.write(resp.getOutputStream());
	}
}
