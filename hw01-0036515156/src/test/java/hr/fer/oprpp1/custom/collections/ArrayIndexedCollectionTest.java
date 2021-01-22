package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArrayIndexedCollectionTest {
	
	private ArrayIndexedCollection aic;
	
	@BeforeEach
	public void init() {
		aic = new ArrayIndexedCollection();
		aic.add("Marko");
		aic.add("Davor");
	}
	
	@Test
	void testConstructor1() {
		ArrayIndexedCollection result = new ArrayIndexedCollection();
		Object[] polje = result.toArray();
		assertEquals(null,polje[0]);
		polje[15] = "Leo";
		assertEquals("Leo",polje[15]);
	}
	
	@Test
	void testConstructor2() {
		ArrayIndexedCollection result = new ArrayIndexedCollection(4);
		Object[] polje = result.toArray();
		polje[0] = 12;
		polje[3] = "Davor";
		assertEquals(12,polje[0]);
		assertEquals("Davor",polje[3]);
	}
	
	@Test
	void testConstructor3() {
		try {
		ArrayIndexedCollection result = new ArrayIndexedCollection(null);
		}catch(NullPointerException ex) {
			return;
		}
	}
	
	@Test
	void testConstructor4() {
		try {
		ArrayIndexedCollection result = new ArrayIndexedCollection(aic,1);
		}catch(IllegalArgumentException ex) {
			return;
		}
	}

	@Test
	void testAdd() {
		aic.add("Marko");
		aic.add(4);
		Object[] polje = aic.toArray();
		assertEquals("Marko",polje[0]);
		assertEquals("Marko",polje[2]);
		assertEquals(4,polje[3]);
	}

	@Test
	void testClear() {
		Object[] polje = aic.toArray();
		assertEquals("Marko",polje[0]);
		aic.clear();
		assertEquals(0,aic.size());
	}

	@Test
	void testGet() {
		Object[] polje = aic.toArray();
		assertEquals("Marko",polje[0]);
		try {
			aic.get(999);
		}catch(IndexOutOfBoundsException ex) {
			return;
		}
	}

	@Test
	void testInsert() {
		try {
			aic.insert("Darko", -1);
		}catch(IndexOutOfBoundsException ex) {
			return;
		}
	}

	@Test
	void testIndexOf() {
		assertEquals(1,aic.indexOf("Davor"));
	}

	@Test
	void testRemoveInt() {
		aic.add("Treci");
		aic.remove(2);
		Object[] polje = aic.toArray();
		assertEquals("Davor",polje[1]);
	}
	
	@Test
	void test() {
		Object[] polje = aic.toArray();
		aic.add("Treci");
		aic.insert("Cetvrti", 3);
		assertEquals("Cetvrti",polje[3]);
		aic.insert("Ja", 1);
		assertEquals("Ja",polje[1]);
		assertEquals("Cetvrti",polje[4]);
		assertEquals(null,polje[5]);
	}
	
}
