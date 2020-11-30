package concurrentSolution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import sequentialSolution.NoSuchDirectoryException;
import sequentialSolution.NullCommandLineArgument;

public class Isi_driver {

  public static void main(String[] args) throws IOException {


    //Making the file so that the producers can write to it
    FileWriter fileWriter;
    BufferedWriter bufferedWriter = null;
    CSVPrinter csvPrinter;
    String path = "/Users/isidoraconic/Desktop/a5_output_files";
    Integer threshold = 1;
    String[] THRESHOLD_OUTPUT_HEADER = {"module_presentation", "date", "total_clicks"};

    try {
      fileWriter = new FileWriter(path + "/" + "activity-" + threshold + ".csv");
      bufferedWriter = new BufferedWriter(fileWriter);
      csvPrinter = new CSVPrinter(bufferedWriter, CSVFormat.DEFAULT.withHeader(THRESHOLD_OUTPUT_HEADER));
    } catch (IOException e) {
      e.printStackTrace();
    }

    csvPrinter = new CSVPrinter(bufferedWriter, CSVFormat.DEFAULT.withSkipHeaderRecord());

    addLine("AAA_2014J", "10", 800, csvPrinter);
    addLine("BBB_2014J", "11", 900, csvPrinter);
    addLine("CCC_2014J", "12", 2000, csvPrinter);
    addLine("DDD_2014J", "13", 8080, csvPrinter);

    try {
      assert bufferedWriter != null;
      bufferedWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }


  }

  public static void addLine(String key, String date, Integer clicks, CSVPrinter printer) {
    FileWriter fileWriter;
    BufferedWriter bufferedWriter = null;
    CSVPrinter csvPrinter;
    String path = "/Users/isidoraconic/Desktop/a5_output_files";
    Integer threshold = 1;
    String[] THRESHOLD_OUTPUT_HEADER = {"module_presentation", "date", "total_clicks"};

    try {
      printer.printRecord(key, date, clicks);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
