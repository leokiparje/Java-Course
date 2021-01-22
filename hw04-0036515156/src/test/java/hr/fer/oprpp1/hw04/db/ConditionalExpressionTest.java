package hr.fer.oprpp1.hw04.db;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ConditionalExpressionTest {
	
	ConditionalExpression expr;
    StudentRecord record;

    @Before
    public void setUp() throws Exception {

        expr = new ConditionalExpression(
                FieldValueGetters.LAST_NAME,
                "Bat*",
                ComparisonOperators.LIKE
        );

        record = new StudentRecord("0036491234", "Baturina", "Mate", 5);
    }

    @Test
    public void testSatisified() {
        assertTrue(expr.getComparisonOperator().satisfied(
                expr.getFieldGetter().get(record),
                expr.getStringLiteral()
        ));
    }
	
}
