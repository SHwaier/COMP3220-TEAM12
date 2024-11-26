
package api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class responsible for parsing data and providing raw records.
 */
public class RecordsCentral {

    private static RecordsCentral instance;

    private RecordsCentral() {
        // private constructor to enforce Singleton pattern
    }

    public static RecordsCentral getInstance() {
        if (instance == null) {
            instance = new RecordsCentral();
        }
        return instance;
    }

    /**
     * Parse the data from the given file path
     * 
     * @param filePath the path to the file to parse
     * @return a RecordsList object containing the parsed records
     * @throws IOException if the file is not found or cannot be read
     */
    public RecordsList parseResourceData(String filePath) {
        InputStream is = RecordsCentral.class.getClassLoader().getResourceAsStream(filePath);
        List<Record> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line = br.readLine(); // Read the header line and ignore it

            // Column headers in order (for mapping)
            String[] headers = line.split(",");

            // Read each subsequent line (each year)
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                String[] values = line.split(",");
                String year = values[0];
                for (int i = 1; i < values.length; i++) {
                    int amount = values[i].equals("..") ? 0 : Integer.parseInt(values[i]);
                    records.add(RecordFactory.createRecord(year, headers[i], amount));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new RecordsList(records);
    }

    public RecordsList parseFileData(String filePath) {
        List<Record> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Read the header line and ignore it

            if (line == null) {
                throw new IOException("File is empty or invalid.");
            }

            // Column headers in order (for mapping)
            String[] headers = line.split(",");

            // Read each subsequent line (each year)
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                String[] values = line.split(",");
                String year = values[0];
                for (int i = 1; i < values.length; i++) {
                    int amount = values[i].equals("..") ? 0 : Integer.parseInt(values[i]);
                    records.add(RecordFactory.createRecord(year, headers[i], amount));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Error parsing numeric value: " + e.getMessage());
        }

        return new RecordsList(records);
    }

}
