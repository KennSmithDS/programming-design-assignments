package sequentialSolution;

import java.io.IOException;

public class SummaryDriver {

  public static void main(String[] args) throws IOException, NoSuchDirectoryException {
    CSVReader file = new CSVReader("/Users/isidoraconic/Desktop/anonymisedData/studentVle_sample.csv");
    CSVWriter writer = new CSVWriter("/Users/isidoraconic/Desktop/a5_output_files/");
    writer.writeFiles(file);
  }

}
