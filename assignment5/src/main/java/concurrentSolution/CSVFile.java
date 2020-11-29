package concurrentSolution;

import java.util.concurrent.CopyOnWriteArrayList;

public class CSVFile {

  private CopyOnWriteArrayList<CopyOnWriteArrayList<String>> rows = new CopyOnWriteArrayList();
  private String name;

  public CSVFile(String name) {
    this.name = name + ".csv";
  }

  public void addRow(CopyOnWriteArrayList<String> row) {
    this.rows.add(row);
  }

  public CopyOnWriteArrayList<CopyOnWriteArrayList<String>> getFileContent() {
    return this.rows;
  }

  public String getName() {
    return this.name;
  }


}
