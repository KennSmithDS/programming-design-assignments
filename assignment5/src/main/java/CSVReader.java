import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.HashMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVReader {

    private final String[] STUDENT_HEADERS = {"module", "presentation", "student", "site", "date", "clicks"};
    private String csvFile;

    public CSVReader(String csvFile) throws FileNotFoundException {
        if (!(new File(csvFile).exists())) {
            throw new FileNotFoundException("The specified CSV file does not exist in provided directory."
            + "Please enter a valid file path.");
        } else {
            this.csvFile = csvFile;
        }
    }

    /**
     * Method that reads the CSV file and returns a data object containing the key lookup
     * for clicks aggregated on module, presentation and date
     * @return HashMap with String key and HashMap of value with Integers as key and value for date and sum clicks
     * @throws IOException default IOException error
     * @throws IllegalArgumentException default IllegalArgumentException error
     */
    public HashMap<String, HashMap<String, Integer>> readCSVFile() throws IOException, IllegalArgumentException {
//        Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
//        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
        Reader csvReader = Files.newBufferedReader(Paths.get(csvFile));
        CSVParser csvParser = new CSVParser(csvReader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withHeader(STUDENT_HEADERS)
//                .withIgnoreHeaderCase()
                );
        HashMap<String, HashMap<String, Integer>> aggStudentData = new HashMap<>();
        for (CSVRecord record : csvParser) {
            String module = record.get("module");
            String presentation = record.get("presentation");
            String codeKey = module + "_" + presentation;
            String date = record.get("date");
            int clicks = Integer.parseInt(record.get("clicks"));

            // module and presentation code exists in HashMap
            if (aggStudentData.containsKey(codeKey)) {
                // date exists in HashMap
                if (aggStudentData.get(codeKey).containsKey(date)) {
                    Integer storedClicks = aggStudentData.get(codeKey).get(date);
                    Integer newClicks = storedClicks + clicks;
                    aggStudentData.get(codeKey).put(date, newClicks);
                } else { //date does not exist in HashMap
                    aggStudentData.get(codeKey).put(date, clicks);
                }
            } else { // module and presentation code don't exist in HashMap
                aggStudentData.put(codeKey, new HashMap<String, Integer>());
                aggStudentData.get(codeKey).put(date, clicks);
            }
        }
        return aggStudentData;
    }

    public static void main(String[] args) throws IOException, IllegalArgumentException {
        String testFile = "anonymisedData/studentVle_sample.csv";
        CSVReader csvReader = new CSVReader(testFile);
        HashMap<String, HashMap<String, Integer>> testData = csvReader.readCSVFile();
        HashMap<String, Integer> testDataRecord = testData.get("AAA_2013J");
        System.out.println(testDataRecord.size());
        for (String key : testDataRecord.keySet()) {
            System.out.println(key + " " + testDataRecord.get(key));
        }
    }

}
