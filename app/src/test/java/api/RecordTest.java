package api;

import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTest {

    @Test
    public void testRecordCreation() {
        Record record = new Record("2020", "Place1", 100);

        assertNotNull(record);
        assertEquals("2020", record.getYear());
        assertEquals("Place1", record.getPlace());
        assertEquals(100, record.getAmount());
    }

    @Test
    public void testToString() {
        Record record = new Record("2021", "Place2", 200);
        String expected = "Record{year=2021, place='Place2', amount=200}";

        assertEquals(expected, record.toString());
    }

    @Test
    public void testGetYear() {
        Record record = new Record("2022", "Place3", 300);

        assertEquals("2022", record.getYear());
    }

    @Test
    public void testGetPlace() {
        Record record = new Record("2023", "Place4", 400);

        assertEquals("Place4", record.getPlace());
    }

    @Test
    public void testGetAmount() {
        Record record = new Record("2024", "Place5", 500);

        assertEquals(500, record.getAmount());
    }
}
