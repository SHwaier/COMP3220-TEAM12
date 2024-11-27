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
     * Parse the data from a resource file in the classpath.
     *
     * @param filePath the path to the resource file
     * @return a RecordsList object containing the parsed records
     * @throws IOException if the resource cannot be found or read
     */
    public RecordsList parseResourceData(String filePath) throws IOException {
        InputStream is = RecordsCentral.class.getClassLoader().getResourceAsStream(filePath);

        if (is == null) {
            throw new IOException("Resource file not found: " + filePath);
        }

        List<Record> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line = br.readLine(); // Read the header line

            if (line == null) {
                throw new IOException("Resource file is empty or invalid: " + filePath);
            }

            // Column headers in order (for mapping)
            String[] headers = line.split(",");

            // Read each subsequent line (each year)
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                String[] values = line.split(",");
                if (values.length != headers.length) {
                    throw new IOException("Malformed data line: " + line);
                }

                String year = values[0];
                for (int i = 1; i < values.length; i++) {
                    int amount = values[i].equals("..") ? 0 : Integer.parseInt(values[i].trim());
                    records.add(RecordFactory.createRecord(year, headers[i], amount));
                }
            }
        } catch (NullPointerException e) {
            throw new IOException("Error parsing resource file!");

        }

        return new RecordsList(records);
    }

    /**
     * Parse the data from a file on the filesystem.
     *
     * @param filePath the path to the file
     * @return a RecordsList object containing the parsed records
     * @throws IOException if the file cannot be found or read
     */
    public RecordsList parseFileData(String filePath) throws IOException {
        List<Record> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Read the header line

            if (line == null) {
                throw new IOException("File is empty or invalid: " + filePath);
            }

            // Column headers in order (for mapping)
            String[] headers = line.split(",");

            // Read each subsequent line (each year)
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                String[] values = line.split(",");
                if (values.length != headers.length) {
                    throw new IOException("Malformed data line: " + line);
                }

                String year = values[0];
                for (int i = 1; i < values.length; i++) {
                    int amount = values[i].equals("..") ? 0 : Integer.parseInt(values[i].trim());
                    records.add(RecordFactory.createRecord(year, headers[i], amount));
                }
            }
        } catch (NumberFormatException e) {
            throw new IOException("Error parsing numeric value: " + e.getMessage(), e);
        }

        return new RecordsList(records);
    }
}
