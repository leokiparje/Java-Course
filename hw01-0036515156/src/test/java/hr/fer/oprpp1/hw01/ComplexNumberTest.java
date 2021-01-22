package hr.fer.oprpp1.hw01;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ComplexNumberTest {
	
	private static final double x = 1E-9;
	
	@Test
    public void fromReal() {
        ComplexNumber c = ComplexNumber.fromReal(4);
        assertEquals(4,c.getReal());
        assertEquals(0,c.getImaginary());
    }
	
	@Test
    public void fromImaginary() {
        ComplexNumber c = ComplexNumber.fromImaginary(4);
        assertEquals(0,c.getReal());
        assertEquals(4,c.getImaginary());
    }
	
	@Test
    public void fromMagnitudeAndAngle() {
        ComplexNumber c = ComplexNumber.fromMagnitudeAndAngle(10, Math.PI);
        assertEquals(-10, c.getReal(), x);
        assertEquals(0,c.getImaginary(), x);
    }
	
	@Test
	public void parseBothDecimal() {
		ComplexNumber c = ComplexNumber.parse("2.652+21.23i");
		assertEquals(2.652, c.getReal(), x);
		assertEquals(21.23, c.getImaginary(), x);
	}
	
	@Test
	public void parseBothDecimalOneNegative() {
		ComplexNumber c = ComplexNumber.parse("2.652-21.23i");
		assertEquals(2.652, c.getReal(), x);
		assertEquals(-21.23, c.getImaginary(), x);
	}
	
	@Test
	public void parseBothDecimalBothNegative() {
		ComplexNumber c = ComplexNumber.parse("-2.652-21.23i");
		assertEquals(-2.652, c.getReal(), x);
		assertEquals(-21.23, c.getImaginary(), x);
	}
	
	@Test
	public void parseOnlyReal() {
		ComplexNumber c = ComplexNumber.parse("2.652");
		assertEquals(2.652, c.getReal(), x);
		assertEquals(0, c.getImaginary(), x);
	}
	
	@Test
	public void parseOnlyImaginary() {
		ComplexNumber c = ComplexNumber.parse("21.23i");
		assertEquals(0, c.getReal(), x);
		assertEquals(21.23, c.getImaginary(), x);
	}
	
	@Test public void getReal() {
		ComplexNumber c = new ComplexNumber(4,5);
		assertEquals(4, c.getReal(), x);
	}
	
	@Test public void getImaginary() {
		ComplexNumber c = new ComplexNumber(4,5);
		assertEquals(5, c.getImaginary(), x);
	}
	
	@Test public void getMagnitude() {
		ComplexNumber c = new ComplexNumber(3,4);
		assertEquals(5, c.getMagnitude(), x);
	}
	
	@Test public void getAngle() {
		ComplexNumber c = new ComplexNumber(4,4);
		assertEquals(Math.PI/4+Math.PI, c.getAngle(), x);
	}
	
	@Test public void addTest() {
		ComplexNumber c1 = new ComplexNumber(4,4);
		ComplexNumber c2 = new ComplexNumber(2.5,3.3);
		ComplexNumber c3 = c1.add(c2);
		assertEquals(6.5, c3.getReal(), x);
		assertEquals(7.3, c3.getImaginary(), x);
	}
	
	@Test public void subTest() {
		ComplexNumber c1 = new ComplexNumber(4,4);
		ComplexNumber c2 = new ComplexNumber(2.5,3.3);
		ComplexNumber c3 = c1.sub(c2);
		assertEquals(1.5, c3.getReal(), x);
		assertEquals(0.7, c3.getImaginary(), x);
	}
	
	@Test public void mulTest() {
		ComplexNumber c1 = new ComplexNumber(4,3);
		ComplexNumber c2 = new ComplexNumber(2,1);
		ComplexNumber c3 = c1.mul(c2);
		assertEquals(5, c3.getReal(), x);
		assertEquals(11, c3.getImaginary(), x);
	}
	
	@Test public void divTest() {
		ComplexNumber c1 = new ComplexNumber(4,3);
		ComplexNumber c2 = new ComplexNumber(2,1);
		ComplexNumber c3 = c1.div(c2);
		assertEquals(2.2, c3.getReal(), x);
		assertEquals(0.4, c3.getImaginary(), x);
	}
	
}





































































