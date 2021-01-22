package hr.fer.oprpp1.math;

import java.util.*;

/**
 * This class represents a complex number with real and imaginary parts of it.
 * @author leokiparje
 *
 */

public class Complex {
	
	private double re;
	private double im;
	
	public static final Complex ZERO = new Complex(0,0);
	public static final Complex ONE = new Complex(1,0);
	public static final Complex ONE_NEG = new Complex(-1,0);
	public static final Complex IM = new Complex(0,1);
	public static final Complex IM_NEG = new Complex(0,-1);
	
	/*
	 * Basic constructor
	 */
	
	public Complex() {
		re=0.;
		im=0.;
	}
	public Complex(double re, double im) {
		this.re=re;
		this.im=im;
	}
	
	public double getReal() {
		return re;
	}
	
	public double getIm() {
		return im;
	}
	
	public double module() {
		return Math.abs(Math.sqrt(re*re+im*im));
	}
	
	public Complex multiply(Complex c) {
		return new Complex(this.re*c.re-this.im*c.im,this.re*c.im+this.im*c.re);
	}
	
	public Complex divide(Complex x) {
		return new Complex((this.re*x.re+this.im*x.im)/(x.re*x.re+x.im*x.im), (this.im*x.re-this.re*x.im)/(x.re*x.re+x.im*x.im));
	}
	
	public Complex add(Complex c) {
		return new Complex(this.re+c.re, this.im+c.im);
	}
	
	public Complex sub(Complex c) {
		return new Complex(this.re-c.re, this.im-c.im);
	}
	
	public Complex negate() {
		return new Complex(-re, -im);
	}
	
	public Complex power(int n) {
		double powerMag = Math.pow(module(), n);

        return new Complex(powerMag * Math.cos(n * Math.atan2(im, re)), powerMag * Math.sin(n * Math.atan2(im, re)));
	}
	
	public static Complex fromMagnitudeAndAngle(double magnitude, double angle) {
		return new Complex(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
	}
	
	public List<Complex> root(int n) {
		List<Complex> roots = new ArrayList<>();
		
		if (n <= 0) throw new IllegalArgumentException();

        double magnitude = Math.pow(module(), 1.0 / n);
        double currentAngle = Math.atan2(im,re)+Math.PI;

        for (int i = 0; i < n; i++) {
            double angle = (currentAngle + 2 * i * Math.PI) / n;
            roots.add(fromMagnitudeAndAngle(magnitude, angle));
        }

        return roots;
	}
	
	/*
	 * This class parses a given String s and returns Complex number if parsing is done corectly
	 */
	
	public static Complex parse(String s) {
		
		s = s.trim();
		double re=0.;
		double im=0.;
		try {
			if (s.indexOf("i")==-1) {
				re = Double.parseDouble(s);
				im = 0.;
			}else {
				if (s.startsWith("i")) {
					re = 0.;
					if (s.indexOf("i")==s.length()-1){
						im = 1.;
					}else {
						im = Double.parseDouble(s.substring(1));
					}
				}else if(s.startsWith("-i")) {
					re = 0.;
					if (s.indexOf("i")==s.length()-1){
						im = -1.;
					}else {
						im = Double.parseDouble(s.substring(2))*-1.;
					}
				}else {
					int index = Math.max(s.substring(1).indexOf("+"), s.substring(1).indexOf("-"));
					re = Double.parseDouble(s.substring(0, ++index).trim());
					if (s.indexOf("i")==s.length()-1) {
						if (s.substring(index+1).indexOf("-")==-1) {
							im = 1.;
						}else {
							im = -1.;
						}
					}else {
						im = Double.parseDouble(s.substring(s.indexOf("i")+1));
						if (s.charAt(index)=='-') im*=-1.;
					}
				}
			}
		}catch(Exception e) {
			System.out.println("Error while parsing complex number.");
		}
		return new Complex(re,im);
	}
	
	public String toString() {
        if (im <  0) return re + "-" + "i"+(-im);
        return re + "+" + "i"+im;
	}
	public static void main(String[] args) {
		System.out.println(parse("-13-2.11i"));
	}
}
