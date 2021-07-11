package hr.fer.oprpp2.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
 * Class Powers is a http servlet which dowloads excel file as response.
 * @author leokiparje
 *
 */

@WebServlet("/powers")
public class Powers extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String as = req.getParameter("a");
		String bs = req.getParameter("b");
		String ns = req.getParameter("n");
		
		int a=0;
		int b=0;
		int n=0;
		
		try {
			a = Integer.parseInt(as);
			b = Integer.parseInt(bs);
			n = Integer.parseInt(ns);
			if ((a<-100 || a>100) || (b<100 || b>100) || (n<1 || n>5)) {
				throw new RuntimeException();
			}
		}catch(Exception e) {
			List<String> messages = new ArrayList<>();
			messages.add("a needs to be from -100 to 100 inclusive.");
			messages.add("b also needs to be from -100 to 100 inclusive.");
			messages.add("n needs to be from 1 to 5 inclusive.");
			req.setAttribute("messages",messages);
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		resp.setContentType("application/octet-stream");
		
		@SuppressWarnings("resource")
		Workbook hwb=new HSSFWorkbook();
		
		for (int i=1;i<=n;i++) {
			HSSFSheet sheet =  (HSSFSheet) hwb.createSheet("Sheet "+i);
			HSSFRow nums = sheet.createRow((short)1);
			HSSFRow powers = sheet.createRow((short)2);
			int cellNum = 0;
			for (int j=a;j<=b;j++) {
				nums.createCell((short)cellNum).setCellValue(j);
				powers.createCell((short)cellNum++).setCellValue(Math.pow(j,i));
			}
		}
		resp.setHeader("content-Disposition","attachment; filename=\"tablica.xls\"");
		hwb.write(resp.getOutputStream());
	}
}















































































