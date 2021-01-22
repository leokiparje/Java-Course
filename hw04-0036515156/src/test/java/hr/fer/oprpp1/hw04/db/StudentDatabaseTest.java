package hr.fer.oprpp1.hw04.db;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class StudentDatabaseTest {
	
	StudentDatabase database;


    @Before
    public void setUp() throws Exception {
        List<String> lines = Files.readAllLines(
                Paths.get("database.txt"),
                StandardCharsets.UTF_8
        );
        database = new StudentDatabase(lines);
    }

    @Test
    public void forJMBAG() {

    }

    @Test
    public void filterAlwaysFalse() {
        List<StudentRecord> filtered = database.filter(record -> false);
        assertEquals(0, filtered.size());
    }

    @Test
    public void filterAlwaysTrue() {
        List<StudentRecord> filtered = database.filter(record -> true);
        assertEquals(63, filtered.size());
    }
	
}
