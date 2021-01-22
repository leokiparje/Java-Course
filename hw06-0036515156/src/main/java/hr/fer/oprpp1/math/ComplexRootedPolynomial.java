package hr.fer.oprpp1.math;

import java.util.*;

/**
 * Class ComplexRootedPolynomial represents complex polynomial initialized with a Complex number constant and roots of given polynomial
 * @author leokiparje
 *
 */

public class ComplexRootedPolynomial {
	
	private Complex constant;
	
	private Complex[] roots;
	
	/*
	 * Basic constructor
	 */
	
	public ComplexRootedPolynomial() {
		super();
	}
	
	/*
	 * Basic constructor
	 */

	public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
		this.constant=constant;
		this.roots = roots;
	}
	
	/*
	 * Method apply takes one complex number and calculates the polynomial by applying the number to the equation.
	 */
	
	public Complex apply(Complex z) {
		
		Complex result = constant;
		
		for (int i=0;i<roots.length;i++) {
			result=result.multiply((z.sub(roots[i])));
		}
		
		return result;
	}
	
	/*
	 * Method toComplexPolynom takes a ComplexRootedPolynom instance and converts it to ComplexPolynomial
	 */
	
	public ComplexPolynomial toComplexPolynom() {

		ComplexPolynomial result = new ComplexPolynomial(constant);
		
		for (int i=0;i<roots.length;i++) {
			ComplexPolynomial helper = new ComplexPolynomial(roots[i].negate(), Complex.ONE);
			result = result.multiply(helper);
		}
		
		return result;
	}
	
	/*
	 * Method toString returns String representation of the object
	 */
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String predznak = constant.getIm()>=0 ? "+" : "-";
		sb.append("("+constant.toString()+")");
		
		for (int i=0;i<roots.length;i++) {
			String znak = roots[i].getIm()>=0 ? "+" : "-";
			sb.append(("*(z-("+roots[i].toString()+"))"));
		}
		
		return sb.toString();
	}
	
	/*
	 * Method returns the index of closest root to a Complex number z giving the treshold.
	 */
	
	public int indexOfClosestRootFor(Complex z, double treshold) {
		int index=0;
		
		for (Complex c : roots) {
			if (z.sub(c).module()<treshold) return index;
			index++;
		}
		
		return -1;
	}
}



































































