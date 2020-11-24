package sequentialSolution;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
 * Class to represent the main file controller that will drive the CSV reading and writing processes
 */
public class Controller {

    /**
     * Main method to drive the program, controls both the CSVReader and CSVWriter classes
     * @param args CLI arguments passed when program runs
     * @throws IOException default IOException error
     * @throws NoSuchDirectoryException custom NoSuchDirectoryException error
     */
    public static void main(String[] args) throws IOException, NoSuchDirectoryException {
        String folderPath = args[0];
        CSVReader reader = new CSVReader(folderPath, "prod");
        CSVWriter writer = new CSVWriter(folderPath);
        writer.writeFiles(reader);
    }
}
