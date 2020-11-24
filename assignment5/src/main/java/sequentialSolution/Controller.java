package sequentialSolution;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
 * Class to represent the main file controller that will drive the CSV reading and writing processes
 * Includes a controller, as well as getters and setters for data objects used
 * E.g. the string file path from CLI argument, aggregated student click data
 */
public class Controller {

    private String folderPath;
    private String fullFilePath;
    private HashMap<String, HashMap<String, Integer>> clickData;
    private static final String STUDENT_CLICKS = "studentVle.csv";
    private static final String STUDENT_CLICKS_TEST = "studentVle_sample.csv";

    /**
     * Constructor method for controller class which takes the String folder path argument
     * Checks if the provided folder path is a valid path on the system before storing property
     * @param argPath String path from CLI argument
     * @throws FileNotFoundException default FileNotFoundException error
     */
    public Controller(String argPath) throws NoSuchDirectoryException {
        if (!(new File(argPath).exists())) {
            throw new NoSuchDirectoryException("The specified folder path does not exist."
                    + "Please enter a valid folder path.");
        } else {
            this.folderPath = argPath;
        }
    }

    public String getFolderPath() { return this.folderPath; }

    /**
     * Method to set the full file path concatenating CLI argument to the hard coded CSV file name
     */
    public void setFilePath() { this.fullFilePath = this.folderPath + '/' + STUDENT_CLICKS_TEST;}

    /**
     * Method to get the full concatenated string file path
     * @return String full path to CSV file for reading
     */
    public String getFilePath() { return this.fullFilePath; }

    /**
     * Method to get the HashMap click data property
     * @return Hashmap object of student click data
     */
    public HashMap<String, HashMap<String, Integer>> getClickData() { return this.clickData; }

    /**
     * Main method to drive the program, controls both the CSVReader and CSVWriter classes
     * @param args CLI arguments passed when program runs
     * @throws IOException default IOException error
     * @throws FileNotFoundException default FileNotFoundException error
     */
    public static void main(String[] args) throws IOException, NoSuchDirectoryException {
        Controller cliControl = new Controller(args[0]);
        cliControl.setFilePath();
        CSVReader reader = new CSVReader(cliControl.getFilePath());
        CSVWriter writer = new CSVWriter(cliControl.getFolderPath());
        writer.writeFiles(reader);
    }
}
