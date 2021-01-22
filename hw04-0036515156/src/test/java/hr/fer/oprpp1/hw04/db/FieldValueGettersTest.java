package hr.fer.oprpp1.hw04.db;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class FieldValueGettersTest {
	
	StudentRecord record;

    @Before
    public void setUp() throws Exception {
        record = new StudentRecord("0036491234", "Obilinović", "Romano", 80);

    }

    @Test
    public void testFirstName() {
        assertEquals("Romano", FieldValueGetters.FIRST_NAME.get(record));
    }

    @Test
    public void testLastName() {
        assertEquals("Obilinović", FieldValueGetters.LAST_NAME.get(record));
    }

    @Test
    public void testJMBAG() {
        assertEquals("0036491234", FieldValueGetters.JMBAG.get(record));
    }
	
}
