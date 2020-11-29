package sequentialSolution;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
 * Class to represent the main file controller that will drive the CSV reading and writing processes
 */
public class SequentialDriver {

    /**
     * Main method to drive the program, controls both the CSVReader and CSVWriter classes
     * @param args CLI arguments passed when program runs
     * @throws IOException default IOException error
     * @throws NoSuchDirectoryException custom NoSuchDirectoryException error
     */
    public static void main(String[] args) throws IOException, NoSuchDirectoryException {
//        String inputFilePath = "/Users/isidoraconic/Desktop/kendall_sample_files"; //= args[0];
//        String outputDir = "/Users/isidoraconic/Desktop/a5_output_files/";
        String inputFilePath = args[0];
        String outputDir = args[0];
        CSVReader reader = new CSVReader(inputFilePath, "test");
        CSVWriter writer = new CSVWriter(outputDir);
        writer.writeFiles(reader);
    }
}
