package hr.fer.oprpp1.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;


public class ComplexTest {

	@Test
    public void module() {
        Complex c = new Complex(3,4);
        assertEquals(5,c.module());
    }
	
	@Test
	public void multiply() {
		Complex c1 = new Complex(3,4);
		Complex c2 = new Complex(3,4);
		assertEquals(new Complex(-7,24), c1.multiply(c2));
	}
	
	@Test
	public void divide() {
		Complex c1 = new Complex(3,4);
		Complex c2 = new Complex(2,5);
		assertEquals(c1.divide(c2) ,new Complex(26/29, -7/29));
	}
	
	@Test
	public void power() {
		Complex c1 = new Complex(3,4);
		assertEquals(c1.power(4), new Complex(-527, -336));
	}
	
}

































































