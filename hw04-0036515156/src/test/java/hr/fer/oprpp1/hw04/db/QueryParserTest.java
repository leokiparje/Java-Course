package hr.fer.oprpp1.hw04.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class QueryParserTest {
	
	@Test
    public void directQuery() {
        QueryParser qp1 = new QueryParser(" jmbag =\"0123456789\" ");
        assertTrue(qp1.isDirectQuery());
        assertEquals("0123456789", qp1.getQueriedJmbag());
        assertEquals(1, qp1.getQuery().size());
    }

    @Test
    public void complexQuery() {
        QueryParser qp2 = new QueryParser("jmbag=\"0123456789\" and lastName>\"J\"");
        assertFalse(qp2.isDirectQuery());
        assertEquals(2, qp2.getQuery().size());
    }
	
}
