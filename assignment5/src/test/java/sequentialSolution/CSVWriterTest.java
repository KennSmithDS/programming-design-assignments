package sequentialSolution;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class CSVWriterTest {

  private CSVWriter writer1;
  private CSVWriter writer2;
  private CSVReader reader;
  private File sampleFile;
  private static final String[] HEADER = {"date", "total_clicks"};
  private static final String DATE_HEADER = "date";
  private static final String CLICKS_HEADER = "total_clicks";

  @Rule
  public TemporaryFolder sampleFileFolder = new TemporaryFolder();

  @Rule
  public TemporaryFolder outputDirectory = new TemporaryFolder();

  @Before
  public void setUp() throws Exception {
    this.sampleFile = sampleFileFolder.newFile("studentVle_test.csv");

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

    PrintWriter csvWriter = new PrintWriter(this.sampleFile);
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

    writer1 = new CSVWriter(outputDirectory.getRoot().getAbsolutePath()+"/");
    reader = new CSVReader(this.sampleFile.toString());
  }

  @Test (expected = NoSuchDirectoryException.class)
  public void invalidConstructor() throws NoSuchDirectoryException {
    writer2 = new CSVWriter("fake_directory");
  }

  @Test
  public void writeFiles() throws IOException {
    writer1.writeFiles(reader);
    Assert.assertEquals(3, outputDirectory.getRoot().listFiles().length);
    File[] outputFiles = outputDirectory.getRoot().listFiles();
    List<File> outputFileList = Arrays.asList(outputFiles);

    //Making a nested HashMap of the expected results
    HashMap<String, HashMap<String, Integer>> expected = new HashMap<>();
    HashMap<String, Integer> AAA_2014J = new HashMap<>();
    HashMap<String, Integer> CCC_2014J_entry1 = new HashMap<>();
    HashMap<String, Integer> CCC_2014J_entry2 = new HashMap<>();
    HashMap<String, Integer> FFF_2014J_entry1 = new HashMap<>();
    HashMap<String, Integer> FFF_2014J_entry2 = new HashMap<>();
    AAA_2014J.put("95", 2);
    CCC_2014J_entry1.put("239", 5);
    CCC_2014J_entry2.put("240", 1);
    FFF_2014J_entry1.put("227", 11);
    FFF_2014J_entry2.put("90", 19);
    expected.put("AAA_2014J.csv", AAA_2014J);
    expected.put("CCC_2014J.csv", CCC_2014J_entry1);
    expected.put("CCC_2014J.csv", CCC_2014J_entry2);
    expected.put("FFF_2014J.csv",FFF_2014J_entry1);
    expected.put("FFF_2014J.csv",FFF_2014J_entry2);


    for(File file : outputFileList) {
      Assert.assertTrue(expected.containsKey(file.getName()));
      Reader csvReader = Files.newBufferedReader(file.toPath());
      CSVParser csvParser = new CSVParser(csvReader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
          .withHeader(HEADER));

      //for(HashMap<String, Integer> data : expected.get(file.getName()))

    }



  }

  /*
  @Test
  public void testEquals() {
  }

  @Test
  public void testHashCode() {
  }

  @Test
  public void testToString() {
  }

   */

  @After
  public void tearDown() {
    sampleFileFolder.delete();
    outputDirectory.delete();
  }
}