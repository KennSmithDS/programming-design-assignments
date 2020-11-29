package concurrentSolution;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import sun.jvm.hotspot.opto.Block;

public class HashMapProducer implements Runnable {

  private BlockingQueue<CSVFile> queue; //= new LinkedBlockingQueue<>();
  private ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> map;
  private final CSVFile POISON;
  private final int N_POISON_PER_PRODUCER;

  public HashMapProducer(BlockingQueue<CSVFile> queue,
      CSVFile poisonPill, ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> map, int N_POISON_PER_PRODUCER) {
    this.queue = queue;
    this.POISON = poisonPill;
    this.map = map;
    this.N_POISON_PER_PRODUCER = N_POISON_PER_PRODUCER;
  }

  /*
  public void getMapElement() throws InterruptedException {
    for(Map.Entry outerKey : this.map.entrySet()) {

      ConcurrentHashMap<String, Integer> innerMap = this.map.remove(outerKey);

      StringBuilder sb1 = new StringBuilder();
      sb1.append(outerKey);
      String codeKey = sb1.toString();

      for(Map.Entry innerKey : this.map.get(outerKey).entrySet()) {

        StringBuilder sb2 = new StringBuilder();
        sb2.append(innerKey);
        String date = sb2.toString();
        sb2 = new StringBuilder();
        sb2.append(this.map.get(outerKey).get(innerKey));
        Integer clicks = Integer.valueOf(sb2.toString());

        //InboundCSVRow(String module, String presentation, Integer student, Integer site, String date, Integer clicks)
        InboundCSVRow row = new InboundCSVRow("module", "presentation",
            Integer.MIN_VALUE, Integer.MIN_VALUE, date, clicks);
        row.setCodeKey(codeKey);
        this.queue.add(row);
      }

      //break;
      //Should I break here? I.e. only does one element at a time?
      //Will this essentially block every single thread other than one from doing this bc it is
      //a concurrent hashmap?
    }
  }
   */

  public CSVFile getMapElement() throws InterruptedException {
    CSVFile outputFile = null;

    System.out.println("The size of the map is now: " + map.size());

    for(String outerKey : this.map.keySet()) {

      ConcurrentHashMap<String, Integer> innerMap = this.map.remove(outerKey);
      //ConcurrentHashMap<String, Integer> innerMap = this.map.get(outerKey);

      StringBuilder sb1 = new StringBuilder();
      sb1.append(outerKey);
      String codeKey = sb1.toString();
      outputFile = new CSVFile(codeKey);

      for(String innerKey : innerMap.keySet()) {
        CopyOnWriteArrayList<String> row = new CopyOnWriteArrayList<>();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(innerKey);
        String date = sb2.toString();
        sb2 = new StringBuilder();
        sb2.append(innerMap.get(innerKey));
        String clicks = sb2.toString();
        row.add(date);
        row.add(clicks);
        outputFile.addRow(row);
      }
      return outputFile;

      //Should I break here? I.e. only does one element at a time?
      //Will this essentially block every single thread other than one from doing this bc it is
      //a concurrent hashmap?
    }
    return outputFile;
  }

  @Override
  public void run() {
    try {
      CSVFile fileInfo;
      while(!map.isEmpty()) {
        fileInfo = getMapElement();
        queue.put(fileInfo);
        System.out.println(Thread.currentThread().getName() + " just added CSVFile = " + fileInfo.getName() + " to the BlockingQueue.");
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      while(true) {
        try {
          for (int i=0; i < this.N_POISON_PER_PRODUCER; i++) {
            System.out.println(Thread.currentThread().getName() + " adding poison pill to queue in WriterProducer!");
            queue.put(this.POISON);
          }
          break;
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        //break;
      }
    }
  }

  public static void main(String[] args) {

    ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> map = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, Integer> a = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, Integer> c = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, Integer> f = new ConcurrentHashMap<>();
    map.put("AAA_2014J.csv", a);
    map.put("CCC_2014J.csv", c);
    map.put("FFF_2014J.csv", f);
    a.put("95", 2);
    c.put("239", 5);
    c.put("240", 1);
    f.put("227", 11);
    f.put("90", 19);

  }


}
