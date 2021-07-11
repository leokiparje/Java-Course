package hr.fer.oprpp2.fractals;

import java.util.ArrayList;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;
import hr.fer.oprpp2.math.Complex;
import hr.fer.oprpp2.math.ComplexPolynomial;
import hr.fer.oprpp2.math.ComplexRootedPolynomial;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

/**
 * Class NewtonP2 represents a fractal viewer. On start it first asks user to input at least two roots and those roots will be used
 * to draw the fractal image. It uses different colors for different roots. Furthermore user can input via configuration arguments
 * number of threads and number of tracks that will be used to calculate the image. Image can be resized and inspected to closer view.
 * @author leokiparje
 *
 */
public class NewtonP2 {
	
	static int minNumberOfJobsDirect;

	public static void main(String[] args) {
		
		setNumberOfThreadsAndJobs(args);
		
		List<Complex> rootsList = new ArrayList<>();
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Minimal number of jobs to be put to direct proccessing : "+minNumberOfJobsDirect); 
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
		
		Complex[] roots = new Complex[rootsList.size()];
		
		int i = 0;
		for (Complex ci : rootsList) {
			roots[i++] =  ci;
		}
		
		sc.close();
		
		System.out.println("Image of fractal will apear shorly. Thank you.");
		FractalViewer.show(new MojProducer(roots));	
		
	}
	
	/*
	 * Static nested class MojProducer is implementation of IFractalProducer interface. It uses implementation of ForkJoinPool
	 *  to do the multi-thread jobs.
	 */
	public static class MojProducer implements IFractalProducer {
		
		Complex[] roots;
		ForkJoinPool pool;
		ComplexRootedPolynomial poly;
		ComplexPolynomial polynom;
		short[] data;
		short order;
		
		public MojProducer(Complex ...polje) {
			roots = polje;
		}
		
		@Override
		public void setup() {
			pool = new ForkJoinPool();
			poly = new ComplexRootedPolynomial(Complex.ONE,roots);
			polynom = poly.toComplexPolynom();
			order = polynom.order();
		}

		@Override
		public void produce(final double reMin, final double reMax, final double imMin, final double imMax,
				final int width, final int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
			
			data = new short[width*height];
			
			System.out.println("Zapocinjem izracun...");
			
			class Posao extends RecursiveAction{

				private static final long serialVersionUID = 1L;
				int tracks;
				int yMin;
				int yMax;
				public Posao(int tracks,int yMin,int yMax) {
					this.tracks=tracks;
					this.yMin = yMin;
					this.yMax = yMax;
				}
				
				@Override
				protected void compute() {
					if (tracks < minNumberOfJobsDirect) {
						computeDirect();
						return;
					}
					Posao p1 = new Posao(tracks/2,yMin,yMax/2);
					Posao p2 = new Posao(tracks - tracks/2,yMax-yMax/2,yMax);
					invokeAll(p1,p2);
				}
				
				private void computeDirect() {
					
					int offset = width*yMin;
					ComplexPolynomial polynom = poly.toComplexPolynom();
					
					for (int j=yMin;j<=yMax;j++) {
						for (int i=0;i<width;i++) {
							
							double re = i / (width-1.0) * (reMax - reMin) + reMin;
							double im = (height-1.0-j) / (height-1) * (imMax - imMin) + imMin;
							
							Complex c = new Complex(re,im);
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
				}
			}
			Posao p = new Posao(height,0,height-1);
			pool.invoke(p);
			
			System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
			observer.acceptResult(data, (short)(order+1), requestNo);
		}
		
		@Override
		public void close() {
			System.out.println("Shutting down pool of threads.");
			pool.shutdown();
		}
	}
	
	/*
	 * Helper method to parse the configuration arguments and set value of minimum number of direct jobs to be processed.
	 */
	private static void setNumberOfThreadsAndJobs(String[] args) {
		if (args.length==0) {
			minNumberOfJobsDirect = 16;
			return;
		}else if (args.length==1) {
			String arg = args[0];
			if (arg.length()>=13 && arg.substring(0, 12).equals("--mintracks=")) {
				try {
					minNumberOfJobsDirect = Integer.parseInt(arg.substring(12,arg.length()));
				}catch(Exception e) {
					System.out.println("Error while parsing.");
				}
			}else {
				throw new IllegalArgumentException("Wrong argument format");
			}
		}else if(args.length==2) {
			String arg1 = args[0];
			if (arg1.equals("-m")) {
				try {
					minNumberOfJobsDirect = Integer.parseInt(args[1]);
				}catch(Exception e) {
					System.out.println("Error while parsing.");
				}
			}else {
				throw new IllegalArgumentException("Wrong argument format");
			}
		}else throw new IllegalArgumentException("Wrong argument format");
	}
}










































