package concurrentSolution;

import sequentialSolution.NoSuchDirectoryException;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVReaderProducer implements Runnable {

    private static final String[] STUDENT_HEADERS = {"module", "presentation", "student", "site", "date", "clicks"};
    private static final String MODULE_HEADER = "module";
    private static final String PRESENTATION_HEADER = "presentation";
    private static final String DATE_HEADER = "date";
    private static final String CLICKS_HEADER = "clicks";
    private static final String STUDENT_CLICKS = "studentVle.csv";
    private static final String STUDENT_CLICKS_TEST = "studentVle_sample.csv";
    private static final String SPLIT_BY = "(?<=\")[^,;](.*?)(?=\")";
    private String csvFile;
    private String folderPath;
    private BlockingQueue<InboundCSVRow> queue;
    private final InboundCSVRow poison;

    CSVReaderProducer(String csvFolder, String mode, BlockingQueue<InboundCSVRow> queue, InboundCSVRow poison) throws NoSuchDirectoryException {
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
            this.queue = queue;
            this.poison = poison;
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

    private ArrayList<String> patternMatch(String inputString) {
        ArrayList<String> parsedString = new ArrayList<>();
        Pattern pattern = Pattern.compile(SPLIT_BY);
        Matcher matcher = pattern.matcher(inputString);
        while (matcher.find()) {
            parsedString.add(matcher.group());
        }
        return parsedString;
    }

    public InboundCSVRow parseCSVRow(String line) {
        ArrayList<String> parsedRow = patternMatch(line);
        String module = parsedRow.get(0);
        String presentation = parsedRow.get(1);
        Integer student = Integer.parseInt(parsedRow.get(2));
        Integer site = Integer.parseInt(parsedRow.get(3));
        String date = parsedRow.get(4);
        Integer clicks = Integer.parseInt(parsedRow.get(5));
        return new InboundCSVRow(module, presentation, student, site, date, clicks);
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new FileReader(this.csvFile))) {
            String headers = reader.readLine();
            String newLine;
            while ((newLine = reader.readLine()) != null) {
                queue.put(parseCSVRow(newLine));
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            while (true) {
                try {
                    queue.put(this.poison);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CSVReaderProducer that = (CSVReaderProducer) o;
        return Objects.equals(csvFile, that.csvFile) &&
                Objects.equals(folderPath, that.folderPath) &&
                Objects.equals(queue, that.queue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(csvFile, folderPath, queue);
    }

    @Override
    public String toString() {
        return "CSVReaderProducer{" +
                "csvFile='" + csvFile + '\'' +
                ", folderPath='" + folderPath + '\'' +
                ", queue=" + queue +
                '}';
    }
}
