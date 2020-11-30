package concurrentSolution;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class CSVFile {

  private CopyOnWriteArrayList<CopyOnWriteArrayList<String>> rows = new CopyOnWriteArrayList();
  private String name;

  public CSVFile(String name) {
    this.name = name + ".csv";
    Random r = new Random();
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CSVFile)) {
      return false;
    }
    CSVFile csvFile = (CSVFile) o;
    return Objects.equals(rows, csvFile.rows) &&
        Objects.equals(name, csvFile.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rows, name);
  }

  @Override
  public String toString() {
    return "CSVFile{" +
        "name='" + name + '\'' +
        '}';
  }
}
