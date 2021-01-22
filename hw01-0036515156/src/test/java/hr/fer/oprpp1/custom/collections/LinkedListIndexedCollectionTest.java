package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class LinkedListIndexedCollectionTest {
	
	private LinkedListIndexedCollection llic;
	
	@BeforeEach
	public void init() {
		llic = new LinkedListIndexedCollection();
		llic.add("Prvi");
		llic.add("Drugi");
	}
	
	@Test
	void testConstructor1() {
		LinkedListIndexedCollection result = new LinkedListIndexedCollection();
		assertEquals(0,result.size());
	}
	
	@Test
	void testConstructor2() {
		assertThrows(NullPointerException.class, () -> {
			LinkedListIndexedCollection result = new LinkedListIndexedCollection(null);
		});
	}
	
	@Test
	void testAdd() {
		assertThrows(NullPointerException.class, () -> {
			llic.add(null);
		});
		Object[] polje = llic.toArray();
		assertEquals("Prvi",polje[0]);
	}
	
	@Test
	void testGet() {
		Object[] polje = llic.toArray();
		assertEquals("Prvi",polje[0]);
		assertThrows(IndexOutOfBoundsException.class, () -> {
			llic.get(-2);
		});
	}
	
	@Test
	void testClear() {
		Object[] polje = llic.toArray();
		assertEquals("Prvi",polje[0]);
		llic.clear();
		assertEquals(0,llic.size());
	}
	
	@Test
	void testInsert() {
		assertThrows(NullPointerException.class, () -> {
			llic.insert(null, 1);
		});
	}
	
	@Test
	void testIndexOf() {
		assertEquals(-1,llic.indexOf("Java"));
	}
	
	@Test
	void testRemoveInt() {
		llic.add("Novi");
		llic.remove(1);
		Object[] polje = llic.toArray();
		assertEquals(2,llic.size());
		assertEquals("Novi", polje[1]);
	}
	
	@Test 
	void test(){
		llic.insert("Nulti", 0);
		llic.insert("Treci", 3);
		llic.add("Cetvrti");
		llic.add("Peti");
		llic.remove("Peti");
		llic.remove("Nulti");
		llic.remove("Prvi");
		llic.remove("Drugi");
		llic.remove("Treci");
		Object[] polje = llic.toArray();
		assertEquals("Cetvrti", polje[0]);		
	}
	
	
}




























































