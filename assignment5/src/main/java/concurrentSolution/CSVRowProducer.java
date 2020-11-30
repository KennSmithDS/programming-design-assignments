package concurrentSolution;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class CSVRowProducer implements Runnable {

  private BlockingQueue<CSVRow> queue;
  private ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> map;
  private CopyOnWriteArrayList<String> keyList;
  private Integer threshold;
  private final CSVRow POISON;
  private final int N_POISON_PER_PRODUCER;

  public CSVRowProducer(BlockingQueue<CSVRow> queue,
      CSVRow poison, ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> map,
      int N_POISON_PER_PRODUCER, CopyOnWriteArrayList<String> keyList, Integer threshold) {
    this.queue = queue;
    this.POISON = poison;
    this.map = map;
    this.N_POISON_PER_PRODUCER = N_POISON_PER_PRODUCER;
    this.keyList = keyList;
    this.threshold = threshold;
  }

  public synchronized void getMapElement() throws InterruptedException {
    String key = keyList.remove(0);
    ConcurrentHashMap<String, Integer> innerMap = this.map.remove(key);

    for(String innerKey : innerMap.keySet()) {
      String date = innerKey;
      Integer clicks = innerMap.get(date);
      if(clicks >= threshold) {
        CSVRow row = new CSVRow(key, date, clicks);
        queue.put(row);
      }
    }
  }

  @Override
  public void run() {
    try {
      CopyOnWriteArrayList<CSVRow> rowList;
      while(!map.isEmpty() && !keyList.isEmpty()) {
        getMapElement();
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
      }
    }
  }

}
