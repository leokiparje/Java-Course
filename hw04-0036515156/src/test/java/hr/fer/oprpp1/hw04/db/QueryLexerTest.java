package hr.fer.oprpp1.hw04.db;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class QueryLexerTest {
	
	@Test
    public void queryBasic() {
        Lexer lexer = new Lexer("jmbag=\"0000000003\"");

        Token correctData[] = {
                new Token("jmbag", TokenType.FIELD),
                new Token("=", TokenType.OPERATOR),
                new Token("0000000003", TokenType.STRING),
                new Token(null, TokenType.EOF)
        };
        checkQueryTokenStream(lexer, correctData);
    }

    @Test
    public void querySpaces() {
        Lexer lexer = new Lexer(" lastName = \"Blažić\"");

        Token correctData[] = {
                new Token("lastName", TokenType.FIELD),
                new Token("=", TokenType.OPERATOR),
                new Token("Blažić", TokenType.STRING),
                new Token(null, TokenType.EOF)
        };
        checkQueryTokenStream(lexer, correctData);
    }

    @Test
    public void querySingleAnd() {
        Lexer lexer = new Lexer("firstName>\"A\" and lastName LIKE \"B*ć\"");

        Token correctData[] = {
                new Token("firstName", TokenType.FIELD),
                new Token(">", TokenType.OPERATOR),
                new Token("A", TokenType.STRING),
                new Token("and", TokenType.AND),
                new Token("lastName", TokenType.FIELD),
                new Token("LIKE", TokenType.OPERATOR),
                new Token("B*ć", TokenType.STRING),

                new Token(null, TokenType.EOF)
        };
        checkQueryTokenStream(lexer, correctData);
    }

    @Test
    public void queryMultipleAnd() {
        Lexer lexer = new Lexer("firstName>\"A\" and firstName<\"C\" and lastName LIKE \"B*ć\" and jmbag>\"0000000002\"");

        Token correctData[] = {
                new Token("firstName", TokenType.FIELD),
                new Token(">", TokenType.OPERATOR),
                new Token("A", TokenType.STRING),
                new Token("and", TokenType.AND),
                new Token("firstName", TokenType.FIELD),
                new Token("<", TokenType.OPERATOR),
                new Token("C", TokenType.STRING),
                new Token("and", TokenType.AND),
                new Token("lastName", TokenType.FIELD),
                new Token("LIKE", TokenType.OPERATOR),
                new Token("B*ć", TokenType.STRING),
                new Token("and", TokenType.AND),
                new Token("jmbag", TokenType.FIELD),
                new Token(">", TokenType.OPERATOR),
                new Token("0000000002", TokenType.STRING),


                new Token(null, TokenType.EOF)
        };
        checkQueryTokenStream(lexer, correctData);
    }


    private void checkQueryTokenStream(Lexer lexer,
                                       Token[] correctData) {
        int counter = 0;
        for (Token expected : correctData) {
            Token actual = lexer.nextToken();
            String msg = "Checking token " + counter + ":";
            assertEquals(msg, expected.getType(), actual.getType());
            assertEquals(msg, expected.getValue(), actual.getValue());
            counter++;
        }
    }
	
}
