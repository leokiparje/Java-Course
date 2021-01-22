package hr.fer.zemris.java.gui.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.AffineTransform;

import javax.swing.JComponent;

/**
 * Class BarChartComponent represents a Component that holds all the information regarding the bar chart.
 * @author leokiparje
 *
 */

public class BarChartComponent extends JComponent {

	private static final long serialVersionUID = 1L;
	
	BarChart barChart;
	
	/*
	 * Basic contructor
	 */
	
	public BarChartComponent(BarChart barChart) {
		this.barChart = barChart;
	}
	
	/*
	 * Method paintComponent is responsible for painting the bar chart
	 */
	
	public void paintComponent(Graphics g) {
		
		Insets insets = getInsets();
		
		Graphics2D g2d = (Graphics2D) g;
		
		int maxNumberWidth = g2d.getFontMetrics().stringWidth(barChart.getyMax()+"");
		
		int py = 50 + 10 + maxNumberWidth + 4 + 4;
		
		g2d.setStroke(new BasicStroke(2));
		
		g2d.drawLine(py, 70, py, getHeight()-76);
		g2d.drawLine(py, 70, py-4, 76);
		g2d.drawLine(py, 70, py+4, 76);
		g2d.drawLine(py, getHeight()-80, getWidth()-70, getHeight()-80);
		g2d.drawLine(getWidth()-70, getHeight()-80, getWidth()-76, getHeight()-76);
		g2d.drawLine(getWidth()-70, getHeight()-80, getWidth()-76, getHeight()-84);
		
		int brojStupaca = barChart.getList().size();
		int brojRedaka = (barChart.getyMax()-barChart.getyMin())/barChart.getRazmak();
		
		int visinaGrafa = getHeight()-160;
		int sirinaGrafa = getWidth()-80-py;
		
		int yMax = barChart.getyMax();
		int yMin = barChart.getyMin();
		
		for (int i=0;i<=brojRedaka;i++) {
			g2d.drawLine(py, getHeight()-80-i*(visinaGrafa/brojRedaka), py-4, getHeight()-80-i*(visinaGrafa/brojRedaka));	
			if (i!=0) {
				g2d.setColor(Color.ORANGE.brighter());
				//g2d.drawLine(py, getHeight()-80-i*(visinaGrafa/brojRedaka), getWidth()-80, getHeight()-80-i*(visinaGrafa/brojRedaka));
				g2d.setColor(Color.BLACK);
			}
			g2d.drawString(""+i*barChart.getRazmak(), py-8-g.getFontMetrics().stringWidth(""+i*barChart.getRazmak()), getHeight()-80-i*(visinaGrafa/brojRedaka));		
		}
		
		for (int i=1;i<=brojStupaca;i++) {
			g2d.drawLine(py+i*(sirinaGrafa/brojStupaca), getHeight()-80, py+i*(sirinaGrafa/brojStupaca), getHeight()-76);	
			g2d.drawString(""+barChart.getList().get(i-1).getX(), py+i*(sirinaGrafa/brojStupaca)-sirinaGrafa/(2*brojStupaca), getHeight()-66);
			
			int rectX = py+1+(i-1)*(sirinaGrafa/brojStupaca);
			int rectY = getHeight()-80-barChart.getList().get(i-1).getY()*visinaGrafa/(yMax-yMin);
			int rectSirina = sirinaGrafa/brojStupaca-1;
			int rectVisina = barChart.getList().get(i-1).getY()*visinaGrafa/(yMax-yMin);
			
			g2d.setColor(Color.ORANGE);
			//g2d.drawLine(py+1+i*(sirinaGrafa/brojStupaca), rectY, py+1+i*(sirinaGrafa/brojStupaca), 80);
			g2d.fillRect(rectX, rectY, rectSirina, rectVisina);
			g2d.setColor(Color.BLACK);
		}
		
		g2d.drawString(barChart.getOpisXos(), getWidth()-sirinaGrafa/2-80-g2d.getFontMetrics().stringWidth(barChart.getOpisXos())/2, getHeight()-50);
		AffineTransform at = new AffineTransform();
		at.rotate(- Math.PI / 2);
		g2d.setTransform(at);
		g2d.drawString(barChart.getOpisYos(), -1*(getHeight()+g2d.getFontMetrics().stringWidth(barChart.getOpisYos()))/2, 50);
      
	}
}



































































































