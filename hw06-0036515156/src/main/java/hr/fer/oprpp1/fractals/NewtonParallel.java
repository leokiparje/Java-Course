package hr.fer.oprpp1.fractals;

import java.util.ArrayList;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import hr.fer.oprpp1.math.Complex;
import hr.fer.oprpp1.math.ComplexPolynomial;
import hr.fer.oprpp1.math.ComplexRootedPolynomial;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

/**
 * Class NewtonParallel displays a drawing on screen based on roots of complex polynomial given as an input.
 * Furthermore it uses more threads and jobs simultaneously to accomplish the job faster than Newton class.
 * @author leokiparje
 *
 */

public class NewtonParallel {

	/**
	 * Method main
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		
		int numberOfThreads = Runtime.getRuntime().availableProcessors();
		int numberOfJobs = 4 * numberOfThreads;
		if (args.length==1) {
			if (args[0].charAt(2)=='w') {
				numberOfThreads = Integer.parseInt(args[0].substring(10));
			}else if (args[0].charAt(2)=='t') {
				numberOfJobs = Integer.parseInt(args[0].substring(9));
			}else throw new IllegalArgumentException();
		} else if (args.length==2) {
			if (args[0].length()==2) {
				if (args[0].charAt(1)=='w') {
					numberOfThreads = Integer.parseInt(args[1]);
				}else if(args[0].charAt(1)=='t') {
					numberOfJobs = Integer.parseInt(args[1]);
				}
			}else {
				numberOfThreads = Integer.parseInt(args[0].substring(10));
				numberOfJobs = Integer.parseInt(args[1].substring(9));
			}
		}else if (args.length==4) {
			numberOfThreads = Integer.parseInt(args[1]);
			numberOfJobs = Integer.parseInt(args[3]);
		}else if (args.length==3) {
			if (args[0].length()==2) {
				numberOfThreads = Integer.parseInt(args[1]);
				numberOfJobs = Integer.parseInt(args[2].substring(9));
			}else if (args[1].length()==2) {
				numberOfThreads = Integer.parseInt(args[0].substring(10));
				numberOfJobs = Integer.parseInt(args[2]);
			}else {
				throw new IllegalArgumentException("Wrong arguments format.");
			}
		}else throw new IllegalArgumentException("Wrong arguments format.");
		
		List<Complex> rootsList = new ArrayList<>();
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Number of workers you chose : "+numberOfThreads); 
		System.out.println("Number of jobs you chose : "+numberOfJobs);
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
		FractalViewer.show(new MojProducer(numberOfThreads, numberOfJobs, roots));	
		
	}
	
	/**
	 * Class PosaoIzracuna represents one thread
	 * @author leokiparje
	 *
	 */

	public static class PosaoIzracuna implements Runnable {
		double reMin;
		double reMax;
		double imMin;
		double imMax;
		int width;
		int height;
		int yMin;
		int yMax;
		int m;
		short[] data;
		Complex[] roots;
		AtomicBoolean cancel;
		ComplexRootedPolynomial poly;
		public static PosaoIzracuna NO_JOB = new PosaoIzracuna();
		
		private PosaoIzracuna() {
		}
		
		public PosaoIzracuna(double reMin, double reMax, double imMin,
				double imMax, int width, int height, int yMin, int yMax, 
				int m, short[] data, Complex[] roots, ComplexRootedPolynomial poly, AtomicBoolean cancel) {
			super();
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.m = m;
			this.data = data;
			this.cancel = cancel;
			this.roots = roots;
			this.poly = poly;
		}
		
		@Override
		public void run() {
			
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
	
	/**
	 * Class MojProducer is used to produce the image via IFractalProducer interface
	 * @author leokiparje
	 *
	 */
	
	public static class MojProducer implements IFractalProducer {
			
		Complex[] roots;
		
		short[] data;
		
		int numberOfThreads;
		
		int numberOfJobs;
		
		public MojProducer(int n, int m, Complex ...polje) {
			numberOfThreads = n;
			numberOfJobs = m;
			roots = polje;
		}
		
		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax,
				int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
			
			if (numberOfJobs>height) {
				numberOfJobs=height;
			}
			
			ComplexRootedPolynomial poly = new ComplexRootedPolynomial(Complex.ONE,roots);
			ComplexPolynomial polynom = poly.toComplexPolynom();
			short order = polynom.order();
			
			data = new short[width*height];
			
			System.out.println("Zapocinjem izracun...");
			
			final int brojTraka = numberOfJobs;
			int brojYPoTraci = height / brojTraka;
			
			final BlockingQueue<PosaoIzracuna> queue = new LinkedBlockingQueue<>();

			Thread[] radnici = new Thread[numberOfThreads];
			for(int i = 0; i < radnici.length; i++) {
				radnici[i] = new Thread(new Runnable() {
					@Override
					public void run() {
						while(true) {
							PosaoIzracuna p = null;
							try {
								p = queue.take();
								if(p==PosaoIzracuna.NO_JOB) break;
							} catch (InterruptedException e) {
								continue;
							}
							p.run();
						}
					}
				});
			}
			for(int i = 0; i < radnici.length; i++) {
				radnici[i].start();
			}
			
			for(int i = 0; i < brojTraka; i++) {
				int yMin = i*brojYPoTraci;
				int yMax = (i+1)*brojYPoTraci-1;
				if(i==brojTraka-1) {
					yMax = height-1;
				}
				PosaoIzracuna posao = new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, yMin, yMax, order+1,
						data, roots, poly, cancel);
				while(true) {
					try {
						queue.put(posao);
						break;
					} catch (InterruptedException e) {
					}
				}
			}
			
			for(int i = 0; i < radnici.length; i++) {
				while(true) {
					try {
						queue.put(PosaoIzracuna.NO_JOB);
						break;
					} catch (InterruptedException e) {
					}
				}
			}
			
			System.out.println("Effective number of threads : "+numberOfThreads);
			System.out.println("Number of jobs : "+numberOfJobs);
			
			for(int i = 0; i < radnici.length; i++) {
				while(true) {
					try {
						radnici[i].join();
						break;
					} catch (InterruptedException e) {
					}
				}
			}
			
			System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
			observer.acceptResult(data, (short)(order+1), requestNo);
		}
	}
}
























































































