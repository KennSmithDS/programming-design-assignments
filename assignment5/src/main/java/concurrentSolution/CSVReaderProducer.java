package concurrentSolution;

import sequentialSolution.NoSuchDirectoryException;

import java.io.File;
import java.util.concurrent.BlockingQueue;

public class CSVReaderProducer implements Runnable {

    private static final String[] STUDENT_HEADERS = {"module", "presentation", "student", "site", "date", "clicks"};
    private static final String MODULE_HEADER = "module";
    private static final String PRESENTATION_HEADER = "presentation";
    private static final String DATE_HEADER = "date";
    private static final String CLICKS_HEADER = "clicks";
    private static final String STUDENT_CLICKS = "studentVle.csv";
    private static final String STUDENT_CLICKS_TEST = "studentVle_sample.csv";
    private String csvFile;
    private String folderPath;
    private BlockingQueue<InboundCSVRow> queue;

    CSVReaderProducer(String csvFolder, String mode) throws NoSuchDirectoryException {
        if (!(new File(csvFolder).exists())) {
            throw new NoSuchDirectoryException("The specified folder path does not exist. "
                    + "Please enter a valid folder path.");
        } else {
            this.folderPath = csvFolder;
            if (mode.equals("test")) {
                this.csvFile = csvFolder + "/" + STUDENT_CLICKS_TEST;
            } else if (mode.equals("prod")) {
                this.csvFile = csvFolder + "/" + STUDENT_CLICKS;
            }
        }
    }

    /**
     * Method to get the folder path passed as command line argument
     * @return String folder path where CSV files are located
     */
    public String getFolderPath() { return this.folderPath; }

    /**
     * Method to get the full concatenated string file path
     * @return String full path to CSV file for reading
     */
    public String getFilePath() { return this.csvFile; }

    @Override
    public void run() {

    }

}
