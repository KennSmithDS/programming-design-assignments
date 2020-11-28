package concurrentSolution;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;

public class WriterConsumer implements Runnable {

  private BlockingDeque<InboundCSVRow> queue;
  private final ConcurrentHashMap<String, Integer> POISON;

  public WriterConsumer(BlockingDeque<InboundCSVRow> queue,
      ConcurrentHashMap<String, Integer> poison) {
    this.queue = queue;
    this.POISON = poison;
  }

  public void writeFile(ConcurrentHashMap<String, Integer> map) {

  }


  @Override
  public void run() {

  }
}
