package hr.fer.oprpp1.fractals;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.oprpp1.math.Complex;
import hr.fer.oprpp1.math.ComplexPolynomial;
import hr.fer.oprpp1.math.ComplexRootedPolynomial;
import hr.fer.zemris.java.fractals.viewer.*;

/**
 * Class Newton displays a drawing on screen based on roots of complex polynomial given as an input
 * @author leokiparje
 *
 */

public class Newton {
	
	/**
	 * Method main
	 * @param args command argument parameters
	 */
	
	public static void main(String[] args) {
		
		List<Complex> rootsList = new ArrayList<>();
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");
		int index=1;
		String c = null;
		while(true) {
			
			System.out.print("Root "+index+++"> ");
			c = sc.nextLine();
			if (c.trim().equalsIgnoreCase("done")) break;
			rootsList.add(Complex.parse(c.trim()));
			
		}
		if (index<3) throw new IllegalArgumentException("Expected at least two roots but got less.");
		
		Complex[] roots = new Complex[rootsList.size()];
		
		int i = 0;
		for (Complex ci : rootsList) {
			roots[i++] =  ci;
		}
		
		System.out.println("Image of fractal will apear shorly. Thank you.");
		FractalViewer.show(new MojProducer(roots));
		
	}
	
	/**
	 * Class MojProducer is used to produce the image via IFractalProducer interface
	 * @author leokiparje
	 *
	 */
			
	public static class MojProducer implements IFractalProducer {
		
		Complex[] roots;
		
		public MojProducer(Complex ...polje) {
			roots = polje;
		}
		
		public void produce(double reMin, double reMax, double imMin, double imMax,
				int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
			
			short[] data = new short[width * height];
			int offset = 0;
			
			short order=0;
			
			ComplexRootedPolynomial poly = new ComplexRootedPolynomial(Complex.ONE,roots);
			ComplexPolynomial polynom = poly.toComplexPolynom();
			order = polynom.order();
			
			for (int j=0;j<height;j++) {
				for (int i=0;i<width;i++) {
					
					Complex c = new Complex(i/(width-1.0) * (reMax - reMin) + reMin,(height-1.0-j) / (height-1) * (imMax - imMin) + imMin);
					Complex zn = c;
					Complex znold;
					int iter = 0;
					double module;
					do {
						Complex numerator = polynom.apply(zn);
						Complex denominator = polynom.derive().apply(zn);
						znold = zn;
						Complex fraction = numerator.divide(denominator);
						zn = zn.sub(fraction);
						module = zn.sub(znold).module();
						iter++;
						
					}while(Math.abs(module)>0.001 && iter<16*16*16);
					int index = poly.indexOfClosestRootFor(zn, 0.001);
					data[offset++] = (short) (index+1);
					
				}
			}
			System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
			observer.acceptResult(data, (short)(order+1), requestNo);
		}	
	}
}



































































