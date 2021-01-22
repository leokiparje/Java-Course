package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.Container;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Class BarChartDemo is program that starts the bar chart drawing
 * @author leokiparje
 *
 */

public class BarChartDemo extends JFrame {

	private static final long serialVersionUID = 1L;
	
	BarChart barChart;
	
	/*
	 * Basic constructor
	 */
	
	public BarChartDemo(BarChart barChart) {
		this.barChart = barChart;
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Bar Chart");
		setLocation(100, 100);
		setSize(400, 400);
		initGUI();
	}
	
	/*
	 * Main method
	 */

	public static void main(String[] args) {
		
		File file = new File(Paths.get(args[0]).toAbsolutePath().toString());
		
		try (Scanner sc = new Scanner(file)){
			
			List<XYValue> l = new ArrayList<>();
			
			String xOs = sc.nextLine();
			String yOs = sc.nextLine();
			String[] list = sc.nextLine().split(" ");
			for (String s : list) {
				try {
					l.add(XYValue.parse(s));
				}catch(Exception e) {
					System.out.println("Greška prilikom parsiranja.");
				}
			}
			int yMin = Integer.parseInt(sc.nextLine());
			int yMax = Integer.parseInt(sc.nextLine());
			int razmak = Integer.parseInt(sc.nextLine());
			
			BarChart chart = new BarChart(l, xOs, yOs, yMin, yMax, razmak);
			
			SwingUtilities.invokeLater(()->{
				BarChartDemo barChartDemo = new BarChartDemo(chart);
			});
			
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	/*
	 * Method initGUI 
	 */
	
	public void initGUI() {
		
		Container cp = getContentPane();
		
		cp.add(new BarChartComponent(barChart));
		
		cp.setBackground(Color.WHITE);
		
	}
}







































































