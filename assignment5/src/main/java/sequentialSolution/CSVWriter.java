package sequentialSolution;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class CSVWriter {

  private String outputDir;
  private List<String> files;
  private static final String[] OUTPUT_HEADER = {"date", "total_clicks"};

  public CSVWriter(String outputDir) throws NoSuchDirectoryException {
    //Check if the output directory exists, if not, throw NoSuchDirectoryException
    if (!(new File(outputDir).exists())) {
      throw new NoSuchDirectoryException("The specified directory does not exist. "
          + "Please enter an existing directory.");
    } else {
      this.outputDir = outputDir;
    }
    this.files = new ArrayList<>();
  }

  public void writeFiles(CSVReader inputFile) throws IOException {
    HashMap<String, HashMap<String, Integer>> info = inputFile.readCSVFile();
    FileWriter fileWriter = null;
    BufferedWriter bufferedWriter = null;
    CSVPrinter csvPrinter = null;

    for (Map.Entry fileName : info.entrySet()) {
      try {
        fileWriter = new FileWriter(this.outputDir + fileName + ".csv");
        bufferedWriter = new BufferedWriter(fileWriter);
        csvPrinter = new CSVPrinter(bufferedWriter, CSVFormat.DEFAULT.withHeader(OUTPUT_HEADER));

        for (Map.Entry date : info.get(fileName).entrySet()) {
          csvPrinter.printRecord(date, info.get(fileName).get(date));
        }

      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          bufferedWriter.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }


}
