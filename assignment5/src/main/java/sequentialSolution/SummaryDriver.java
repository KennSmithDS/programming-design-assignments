package sequentialSolution;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class SummaryDriver {

  public static void main(String[] args) throws IOException, NoSuchDirectoryException {

    //Writing a CSV file in Java adapted from the following link:
    //https://stackabuse.com/reading-and-writing-csvs-in-java/
    List<List<String>> rows = Arrays.asList(
        Arrays.asList("10174528", "FFF", "2014J", "646551", "882597", "227", "3"),
        Arrays.asList("4613985", "FFF", "2014J", "487314", "798620", "227", "8"),
        Arrays.asList("964702", "FFF", "2014J", "511678", "703741", "90", "19"),
        Arrays.asList("3099664", "CCC",	"2014J",	"645334",	"909098",	"240",	"1"),
        Arrays.asList("3091971",	"CCC",	"2014J",	"644246",	"909088",	"239",	"5"),
        Arrays.asList("265522",	"AAA",	"2014J",	"323370",	"877026",	"95",	"2")
    );

    PrintWriter csvWriter = new PrintWriter("/Users/isidoraconic/Desktop/a5_test/template_test.csv");
    csvWriter.append("index");
    csvWriter.append(",");
    csvWriter.append("code_module");
    csvWriter.append(",");
    csvWriter.append("code_presentation");
    csvWriter.append(",");
    csvWriter.append("id_student");
    csvWriter.append(",");
    csvWriter.append("id_site");
    csvWriter.append(",");
    csvWriter.append("date");
    csvWriter.append(",");
    csvWriter.append("sum_click");
    csvWriter.append("\n");

    for (List<String> rowData : rows) {
      csvWriter.append(String.join(",", rowData));
      csvWriter.append("\n");
    }

    csvWriter.flush();
    csvWriter.close();


    CSVReader file = new CSVReader("/Users/isidoraconic/Desktop/a5_test/template_test.csv");
    CSVWriter writer = new CSVWriter("/Users/isidoraconic/Desktop/a5_output_files/");
    writer.writeFiles(file);



  }

}
