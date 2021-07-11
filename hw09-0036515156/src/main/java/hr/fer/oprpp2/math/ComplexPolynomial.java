package hr.fer.oprpp2.math;

/**
 * Class ComplexPolynomial represents a polynomial of complex numbers
 * @author leokiparje
 *
 */

public class ComplexPolynomial {
	
	private Complex[] factors;
	
	/*
	 * Basic constructor
	 */
	
	public ComplexPolynomial() {
		super();
	}
	
	/*
	 * Basci constructor
	 */
	
	public ComplexPolynomial(Complex ...factors) {
		this.factors = new Complex[factors.length];
		int i=0;
		for (Complex c : factors) {
			this.factors[i++] = c;
		}
	}
	
	/*
	 * Method returns the order of the polynomial
	 */
	
	public short order() {
		return (short) (factors.length - 1);
	}
	
	/*
	 * Method multiplies given ComplexPolynomial with another one
	 */
	
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		
		
		Complex[] newFactors = new Complex[order() + p.order() + 1];

        for (int i = 0; i < newFactors.length; i++) {
            newFactors[i] = Complex.ZERO;
        }

        Complex[] pFactors = p.factors;

        for (int i = 0; i < factors.length; i++) {
            for (int j = 0; j < pFactors.length; j++) {
                newFactors[i + j] = newFactors[i + j].add(factors[i].multiply(pFactors[j]));
            }
        }

        return new ComplexPolynomial(newFactors);
		
	}
	
	/*
	 * Method calculated the derivate of the polynom
	 */
	
	public ComplexPolynomial derive() {
		
		Complex[] facts = new Complex[order()];
		
		for (int i=1;i<facts.length+1;i++) {
			facts[i-1] = factors[i].multiply(new Complex(i,0));
		}
		
		return new ComplexPolynomial(facts);
	}
	
	/*
	 * Method apllies Complex takes one complex number and resolves the polynomial equation
	 */
	
	public Complex apply(Complex z) {
		
		Complex result = Complex.ZERO;
		
		for (int i=0;i<factors.length;i++) {
			result = result.add(z.power(i).multiply(factors[i]));
		}
		
		return result;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (int i=factors.length-1;i>=0;i--) {
			sb.append("("+factors[i].toString()+")" + (i>0 ? "*z^"+i : "")+(i>0 ? "+" : ""));
		}
		
		return sb.toString();
	}
}






































































