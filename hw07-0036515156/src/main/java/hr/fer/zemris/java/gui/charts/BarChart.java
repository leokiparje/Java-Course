package hr.fer.zemris.java.gui.charts;

import java.util.ArrayList;
import java.util.List;

/**
 * Class BarChart represents a bar chart.
 * @author leokiparje
 *
 */

public class BarChart {
	
	private List<XYValue> list;
	
	private String opisXos;
	private String opisYos;
	
	private int yMin;
	private int yMax;
	private int razmak;
	
	/*
	 * Basic contructor
	 */

	public BarChart(List<XYValue> list, String opisXos, String opisYos, int yMin, int yMax, int razmak) {
		
		if (yMin<0) throw new IllegalArgumentException("Minimalni y mora biti pozitivan broj.");
		if (yMax<=yMin) throw new IllegalArgumentException("Maksimalni y mora biti strogo veći od minimalnog y.");
		
		this.opisXos = opisXos;
		this.opisYos = opisYos;
		this.yMin = yMin;
		this.yMax = yMax;
		if ((yMax-yMin)%razmak!=0) {
			this.razmak = (int) Math.ceil(yMin+razmak);
		}else {
			this.razmak = razmak;
		}
		for (XYValue v : list) {
			if (v.getY()<yMin) throw new IllegalArgumentException("Podatci u listi ne odgovaraju kriteriju.");
		}
		this.list = list;
	}

	/**
	 * @return the list
	 */
	public List<XYValue> getList() {
		return list;
	}

	/**
	 * @return the opisXos
	 */
	public String getOpisXos() {
		return opisXos;
	}

	/**
	 * @return the opisYos
	 */
	public String getOpisYos() {
		return opisYos;
	}

	/**
	 * @return the yMin
	 */
	public int getyMin() {
		return yMin;
	}

	/**
	 * @return the yMax
	 */
	public int getyMax() {
		return yMax;
	}

	/**
	 * @return the razmak
	 */
	public int getRazmak() {
		return razmak;
	}
	
	
	
}


























































































