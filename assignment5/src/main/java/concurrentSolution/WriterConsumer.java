package concurrentSolution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import sequentialSolution.NoSuchDirectoryException;

public class WriterConsumer implements Runnable {

  private BlockingQueue<CSVFile> queue;
  private String outputDir;
  private static final String[] OUTPUT_HEADER = {"date", "total_clicks"};
  private final CSVFile POISON;

  public WriterConsumer(String outputDir, BlockingQueue<CSVFile> queue,
                        CSVFile poison) throws NoSuchDirectoryException {
    //Check if the output directory exists, if not, throw NoSuchDirectoryException
    if (!(new File(outputDir).exists())) {
      throw new NoSuchDirectoryException("The specified directory does not exist. "
              + "Please enter an existing directory.");
    } else {
      this.outputDir = outputDir;
    }
    this.queue = queue;
    this.POISON = poison;
  }

  public void writeFile(CSVFile fileContent) throws IOException {

    FileWriter fileWriter;
    BufferedWriter bufferedWriter = null;
    CSVPrinter csvPrinter;

    try {
      fileWriter = new FileWriter(this.outputDir + "/" + fileContent.getName());
      bufferedWriter = new BufferedWriter(fileWriter);
      csvPrinter = new CSVPrinter(bufferedWriter, CSVFormat.DEFAULT.withHeader(OUTPUT_HEADER));

      for (CopyOnWriteArrayList<String> row : fileContent.getFileContent()) {
        csvPrinter.printRecord(row.get(0), row.get(1));
      }

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        assert bufferedWriter != null;
        bufferedWriter.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }


  @Override
  public void run() {
    try {
      CSVFile outputFile;
      while (!(outputFile = queue.take()).equals(POISON)) {
        System.out.println(Thread.currentThread().getName() + " taking " + outputFile.getName() + " from BlockingQueue");
        writeFile(outputFile);
      }
    } catch (InterruptedException | IOException interruptedException) {
      Thread.currentThread().interrupt();
    }
  }
}
