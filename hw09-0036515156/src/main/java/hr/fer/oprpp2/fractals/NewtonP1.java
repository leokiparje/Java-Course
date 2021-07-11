package hr.fer.oprpp2.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.oprpp2.math.Complex;
import hr.fer.oprpp2.math.ComplexPolynomial;
import hr.fer.oprpp2.math.ComplexRootedPolynomial;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

/**
 * Class NewtonP1 represents a fractal viewer. On start it first asks user to input at least two roots and those roots will be used
 * to draw the fractal image. It uses different colors for different roots. Furthermore user can input via configuration arguments
 * number of threads and number of tracks that will be used to calculate the image. Image can be resized and inspected to closer view.
 * @author leokiparje
 *
 */
public class NewtonP1 {
	
	static int numberOfThreads;
	static int numberOfJobs;

	/*
	 * Main method
	 */
	public static void main(String[] args) {
		
		numberOfThreads = Runtime.getRuntime().availableProcessors();
		numberOfJobs = 4 * numberOfThreads;
		
		setNumberOfThreadsAndJobs(args);
		
		List<Complex> rootsList = new ArrayList<>();
		
		Scanner sc = new Scanner(System.in);
		
		int actualNumberOfProcessors = Runtime.getRuntime().availableProcessors();
		if (Runtime.getRuntime().availableProcessors()>numberOfThreads) {
			actualNumberOfProcessors = numberOfThreads;
		}
		
		System.out.println("Number of workers you chose : "+numberOfThreads);
		System.out.println("Number of processors you actually use : "+actualNumberOfProcessors);
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
	
	/*
	 * Static nested class PosaoIzracuna
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
	
	/*
	 * Static nested class MojProducer is implementation of IFractalProducer interface. It uses implementation of Executors with fixed
	 * number of workers to do the multi-thread jobs.
	 */
	public static class MojProducer implements IFractalProducer {
		
		Complex[] roots;
		ComplexRootedPolynomial poly;
		ComplexPolynomial polynom;
		short[] data;
		short order;
		int numberOfThreads;
		int numberOfJobs;
		ExecutorService pool;
		
		public MojProducer(int numberOfThreads, int numberOfJobs, Complex ...polje) {
			this.numberOfThreads = numberOfThreads;
			this.numberOfJobs = numberOfJobs;
			roots = polje;
		}
		
		@Override
		public void setup() {
			pool = Executors.newFixedThreadPool(numberOfThreads);
			poly = new ComplexRootedPolynomial(Complex.ONE,roots);
			polynom = poly.toComplexPolynom();
			order = polynom.order();
		}

		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax,
				int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
			
			System.out.println("Broj korištenih traka : "+numberOfJobs);
			
			data = new short[width*height];
			
			System.out.println("Zapocinjem izracun...");
			
			final int brojTraka = numberOfJobs;
			int brojYPoTraci = height / brojTraka;
			
			PosaoIzracuna[] poslovi = new PosaoIzracuna[numberOfJobs];
			for (int i=0;i<numberOfJobs;i++) {
				int yMin = i*brojYPoTraci;
				int yMax = (i+1)*brojYPoTraci-1;
				if(i==brojTraka-1) {
					yMax = height-1;
				}
				poslovi[i] = new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, yMin, yMax, order+1,
						data, roots, poly, cancel);
			}
			List<Future<?>> rezultati = new ArrayList<Future<?>>();
			
			for (PosaoIzracuna p : poslovi) {
				rezultati.add(pool.submit(p));
			}
			
			for (Future<?> f : rezultati) {
				while(true) {
					try {
						f.get();
						break;
					} catch (InterruptedException | ExecutionException e) {
					}
				}
			}
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
	 * Helper method to parse the configuration arguments and set value to number of workers and tracks to be used.
	 */
	private static void setNumberOfThreadsAndJobs(String[] args) {
		if (args.length==0) return;
		else if (args.length==1) {
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
				if (args[0].length()<10) throw new IllegalArgumentException("Wrong arguments format.");
				if (args[0].substring(0, 8).equals("--tracks")) {
					if (args[1].substring(0,8).equals("--tracks")) throw new IllegalArgumentException("Wrong arguments exception.");
					numberOfJobs = Integer.parseInt(args[0].substring(9));
					numberOfThreads = Integer.parseInt(args[1].substring(10));
				}else {
					if (args[0].substring(0, 9).equals("--workers")) throw new IllegalArgumentException("Wrong arguments format.");
					numberOfThreads = Integer.parseInt(args[0].substring(10));
					numberOfJobs = Integer.parseInt(args[1].substring(9));
				}
			}
		}else if (args.length==4) {
			if (args[0].equals("-w")) {
				if (args[2].equals("-w")) throw new IllegalArgumentException("Wrong arguments format.");
				numberOfThreads = Integer.parseInt(args[1]);
				numberOfJobs = Integer.parseInt(args[3]);
			}else {
				if (args[2].equals("-t")) throw new IllegalArgumentException("Wrong arguments format.");
				numberOfJobs = Integer.parseInt(args[1]);
				numberOfThreads = Integer.parseInt(args[3]);
			}
		}else if (args.length==3) {
			if (args[0].length()==2) {
				if (args[0].equals("-w")) {
					if (args[1].substring(0,9).equals("--workers")) throw new IllegalArgumentException("Wrong arguments format.");
					numberOfThreads = Integer.parseInt(args[1]);
					numberOfJobs = Integer.parseInt(args[2].substring(9));
				}else {
					if (args[1].substring(0,8).equals("--tracks")) throw new IllegalArgumentException("Wrong arguments format.");
					numberOfJobs = Integer.parseInt(args[1]);
					numberOfThreads = Integer.parseInt(args[2].substring(10));
				}
			}else if (args[1].length()==2) {
				if (args[0].substring(0,9).equals("--workers")) {
					if (args[1].equals("-w")) throw new IllegalArgumentException("Wrong arguments format.");
					numberOfThreads = Integer.parseInt(args[0].substring(10));
					numberOfJobs = Integer.parseInt(args[2]);
				}else {
					if (args[1].equals("-t")) throw new IllegalArgumentException("Wrong arguments format.");
					numberOfJobs = Integer.parseInt(args[0].substring(9));
					numberOfThreads = Integer.parseInt(args[2]);
				}
			}else {
				throw new IllegalArgumentException("Wrong arguments format.");
			}
		}else throw new IllegalArgumentException("Wrong arguments format.");
	}
	
}
