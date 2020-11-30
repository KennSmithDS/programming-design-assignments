package concurrentSolution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import sequentialSolution.NoSuchDirectoryException;

public class CSVRowWriterConsumer implements Runnable {

  private BlockingQueue<CSVRow> queue;
  private static final String[] OUTPUT_HEADER = {"module_presentation", "date", "total_clicks"};
  private final CSVRow POISON;
  private Integer threshold;
  private CSVPrinter printer;

  public CSVRowWriterConsumer(BlockingQueue<CSVRow> queue,
      CSVRow poison, Integer threshold, CSVPrinter printer)  {

    this.queue = queue;
    this.POISON = poison;
    this.threshold = threshold;
    this.printer = printer;
  }

  /**
   * Method that takes a CSVPrinter object from its field, and prints the passed CSVRow object to the file
   * @param row
   * @throws IOException
   */
  public void writeFile(CSVRow row) throws IOException {
    try {
      printer.printRecord(row.getCodeKey(), row.getDate(), row.getClicks());
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void run() {
    try {
      CSVRow row;
      while (!(row = queue.take()).equals(POISON)) {
        writeFile(row);
        System.out.println(Thread.currentThread().getName() + " taking row from BlockingQueue, codekey =  " + row.getCodeKey() + " and clicks = " + row.getClicks());
      }
    } catch (InterruptedException | IOException interruptedException) {
      Thread.currentThread().interrupt();
      System.out.println(Thread.currentThread().getName() + " was interrupted!");
    }

  }
}
