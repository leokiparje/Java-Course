package hr.fer.oprpp2.servlets;

import java.io.IOException;
import java.io.OutputStream;

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
 * Class OsUsage is a http servlet which draws a chart.
 * @author leokiparje
 *
 */

@WebServlet("/osUsage")
public class OsUsage extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType("image/png");
		
		JFreeChart chart = getChart();
		
		resp.getOutputStream().write(ChartUtils.encodeAsPNG(chart.createBufferedImage(500,500)));
	}
	
	private JFreeChart getChart() {
		
		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
		
		dataset.setValue("Croatia", 22);
        dataset.setValue("Bohemia", 34);
        dataset.setValue("Bulgaria", 18);
        dataset.setValue("Spain", 5);
        dataset.setValue("Others", 21);
		
		JFreeChart chart = ChartFactory.createPieChart("OS usage",dataset,true,false,false);
		chart.setBorderVisible(false);
		return chart;
	}
}
