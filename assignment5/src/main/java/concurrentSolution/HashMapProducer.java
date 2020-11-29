package concurrentSolution;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class HashMapProducer implements Runnable {

  private BlockingQueue<CSVFile> queue; //= new LinkedBlockingQueue<>();
  private ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> map;
  private CopyOnWriteArrayList<String> keyList;
  private final CSVFile POISON;
  private final int N_POISON_PER_PRODUCER;

  public HashMapProducer(BlockingQueue<CSVFile> queue,
      CSVFile poisonPill, ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> map,
      int N_POISON_PER_PRODUCER, CopyOnWriteArrayList<String> keyList) {
    this.queue = queue;
    this.POISON = poisonPill;
    this.map = map;
    this.N_POISON_PER_PRODUCER = N_POISON_PER_PRODUCER;
    this.keyList = keyList;
  }


  public synchronized CSVFile getMapElement() throws InterruptedException {
    String key = keyList.remove(0);
    ConcurrentHashMap<String, Integer> innerMap = this.map.remove(key);
    CSVFile outputFile = new CSVFile(key);

    for(String innerKey : innerMap.keySet()) {
      CopyOnWriteArrayList<String> row = new CopyOnWriteArrayList<>();
      String date = innerKey;
      String clicks = "" + innerMap.get(date);
      row.add(date);
      row.add(clicks);
      outputFile.addRow(row);
    }

    return  outputFile;

  }

  @Override
  public void run() {
    try {
      CSVFile fileInfo;
      while(!map.isEmpty() && !keyList.isEmpty()) {
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
