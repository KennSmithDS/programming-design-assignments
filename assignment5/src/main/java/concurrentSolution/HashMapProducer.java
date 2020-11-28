package concurrentSolution;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import sun.jvm.hotspot.opto.Block;

public class HashMapProducer implements Runnable {

  private BlockingQueue<ConcurrentHashMap<String, Integer>> queue; //= new LinkedBlockingQueue<>();
  private ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> map;
  private final ConcurrentHashMap<String, Integer> POISON;

  public HashMapProducer(BlockingQueue<ConcurrentHashMap<String, Integer>> queue,
      ConcurrentHashMap<String, Integer> poisonPill, ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> map) {
    this.queue = queue;
    this.POISON = poisonPill;
    this.map = map;
  }

  public void getMapElement() throws InterruptedException {
    for(Map.Entry outerKey : this.map.entrySet()) {

      //Should this be map.get?
      ConcurrentHashMap<String, Integer> innerMap = this.map.remove(outerKey);
      this.queue.add(innerMap);
      //break;
      //Should I break here? I.e. only does one element at a time?
      //Will this essentially block every single thread other than one from doing this bc it is
      //a concurrent hashmap?
    }
  }

  @Override
  public void run() {

    try {
      getMapElement();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      while(true) {
        try {
          this.queue.put(this.POISON);
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
