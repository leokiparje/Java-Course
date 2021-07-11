package hr.fer.oprpp2.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class Trigonometric is a http servlet which displays sinus and cosinus values for given range of numbers.
 * @author leokiparje
 *
 */

@WebServlet("/trigonometric")
public class Trigonometric extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public class Angle {
		
		private int angle;
		private double sin;
		private double cos;
		
		public Angle(int angle) {
			this.angle = angle;
			calculate();
		}
		
		private double toRad(int angle) {
			return angle*Math.PI/180;
		}
		
		private void calculate() {
			this.sin = Math.sin(toRad(angle));
			this.cos = Math.cos(toRad(angle));
		}
		
		public int getAngle() {
			return angle;
		}
		
		public double getSin() {
			return sin;
		}
		
		public double getCos() {
			return cos;
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String as = (String) req.getParameter("a");
		String bs = (String) req.getParameter("b");
		
		int a;
		int b;
		
		try {
			a = Integer.parseInt(as);
		}catch(Exception e) { a=0; }
		
		try {
			b = Integer.parseInt(bs);
		}catch(Exception e) { b=360; }
		
		if (a>b) {
			int help = a;
			a = b;
			b = help;
		}
		if (b>a+720) {
			b = a+720;
		}
		
		List<Angle> angleList = new ArrayList<>();

		
		for (int i=a;i<b;i++) {
			angleList.add(new Angle(i));
		}
		
		req.setAttribute("angleList",angleList);

		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}
}
