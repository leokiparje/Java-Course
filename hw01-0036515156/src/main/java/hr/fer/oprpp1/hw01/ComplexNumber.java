package hr.fer.oprpp1.hw01;

/**
 * Class that represents complex number
 * @author leokiparje
 *
 */
public class ComplexNumber {
	
	/**
	 * double representation of real part of the complex number
	 */
	private double r;
	
	/**
	 * imaginary representation of the complex nubmer
	 */
	private double i;
	
	/**
	 * constructor
	 * @param r real part of the complex number
	 * @param i imaginary part of the complex number
	 */
	public ComplexNumber(double r, double i) {
		this.r=r;
		this.i=i;
	}
	
	/**
	 * takes real part of the complex number
	 * @param real real part of the complex number
	 * @return newly created complex number with imaginary part set to 0
	 */
	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real,0);
	}
	
	/**
	 * takes imaginary part of the complex number
	 * @param imaginary real part of the complex number
	 * @return newly created complex number with real part set to 0
	 */
	public static ComplexNumber fromImaginary(double imaginary) {
		return new ComplexNumber(0, imaginary);
	}
	
	/**
	 * creates a complex number from given magnitude and angle
	 * @param magnitude is radius of the complex number
	 * @param angle is angle of the complex number
	 * @return newly created complex number with real and imaginary parts
	 */
	public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
		return new ComplexNumber(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
	}
	
	/**
	 * parses the complex number
	 * @param s String that is being parsed
	 * @return Parsed complex number
	 */
	public static ComplexNumber parse(String s) {
		
		boolean fPos = true;
		boolean sPos = true;
		if (s.charAt(0) == '-')     
		    fPos = false;
		if (s.substring(1).contains("-"))
		    sPos = false;
		String[] split = s.split("-|\\+");
		if (split[0].equals("")) {  
		    split[0] = split[1];
		    split[1] = split[2];
		}
		double r = 0;
		double i = 0;
		if (split[0].contains("i")) 
			if (fPos) {
				i = Double.parseDouble("+"+split[0].substring(0, split[0].length()-1));
			}else {
				i = Double.parseDouble("-"+split[0].substring(0, split[0].length()-1));
			}
		else
			if (fPos) {
				r = Double.parseDouble("+" + split[0]);
			}else {
				r = Double.parseDouble("-" + split[0]);
			}
		if (split.length > 1) {     
		    if (split[1].contains("i"))
		    	if (sPos) {
		    		i = Double.parseDouble("+" + split[1].substring(0,split[1].length()-1));
		    	}else {
		    		i = Double.parseDouble("-" + split[1].substring(0,split[1].length()-1));
		    	}
		    else
		    	if (sPos) {
		    		r = Double.parseDouble("+" + split[1]);
		    	}else {
		    		r = Double.parseDouble("-" + split[1]);
		    	}
		}
		return new ComplexNumber(r, i);
		
	}
	
	/**
	 * returns real part of the complex number
	 */
	public double getReal() {
		return r;
	}
	
	/**
	 * 
	 * @return imaginary part of the complex number
	 */
	public double getImaginary() {
		return i;
	}
	
	/**
	 * 
	 * @return magnitude calculated from real and imaginary part of the complex number
	 */
	public double getMagnitude() {
		return Math.sqrt(r*r+i*i);
	}
	
	/**
	 * 
	 * @return angle calculated from real and imaginary part of the complex number
	 */
	public double getAngle() {
		return Math.atan2(i,r)+Math.PI;
	}
	
	/**
	 * adds two complex numbers
	 * @param c other complex number that is being added
	 * @return newly created instance of Complex Number
	 */
	public ComplexNumber add(ComplexNumber c) {
		return new ComplexNumber(r+c.getReal(), i+c.getImaginary());
	}
	
	/**
	 * subtracts a complex number from the given one
	 * @param c other complex number that will be the subtractor
	 * @return newly created instance of Complex Number
	 */
	public ComplexNumber sub(ComplexNumber c) {
		return new ComplexNumber(r-c.getReal(), i-c.getImaginary());
	}
	
	/**
	 * multiplies two complex numbers
	 * @param c other complex number
	 * @return returns newly created ComplexNumber that is a result of multiplication
	 */
	public ComplexNumber mul(ComplexNumber c) {
		return new ComplexNumber(r*c.getReal()-i*c.getImaginary(),r*c.getReal()+i*c.getImaginary());
	}
	
	/**
	 * divides two complex numbers
	 * @param c other complex number
	 * @return newly created complex number
	 */
	public ComplexNumber div(ComplexNumber c) {
		double denominator = c.r * c.r + c.i * c.i;

        if (Double.compare(denominator, 0.0) == 0) {
            throw new IllegalArgumentException();
        }

        double realNumerator = r * c.r + i * c.i;
        double imaginaryNumerator = i * c.r - r * c.i;
        return new ComplexNumber(realNumerator / denominator, imaginaryNumerator / denominator);
	}
	
	public ComplexNumber power(int n) {
		if (n<0) throw new IllegalArgumentException();
		
		double magnitude = Math.pow(getMagnitude(), n);
        double angle = getAngle() * n;

        while (angle > (2 * Math.PI)) {
            angle -= 2 * Math.PI;
        }

        return fromMagnitudeAndAngle(magnitude, angle);
	}
	
	public ComplexNumber[] root(int n) {
		if (n <= 0) {
            throw new IllegalArgumentException();
        }

        double magnitude = Math.pow(getMagnitude(), 1.0 / n);
        double currentAngle = getAngle();

        ComplexNumber[] roots = new ComplexNumber[n];

        for (int i = 0; i < n; i++) {
            double angle = (currentAngle + 2 * i * Math.PI) / n;
            roots[i] = fromMagnitudeAndAngle(magnitude, angle);
        }

        return roots;
	}
	
	public String toString() {
		if (i == 0) return r + "";
        if (r == 0) return i + "i";
        if (i <  0) return r + " - " + (-i) + "i";
        return r + " + " + i + "i";
	}

}










































































