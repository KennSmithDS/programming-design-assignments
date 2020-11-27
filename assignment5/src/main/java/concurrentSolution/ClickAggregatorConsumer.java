package concurrentSolution;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;

public class ClickAggregatorConsumer implements Runnable {

    private BlockingDeque<InboundCSVRow> queue;
    private ConcurrentHashMap<String, ConcurrentHashMap<Integer, Integer>> clickData;

    ClickAggregatorConsumer(BlockingDeque<InboundCSVRow> queue, ConcurrentHashMap<String, ConcurrentHashMap<Integer, Integer>> clickData) {
        this.queue = queue;
        this.clickData = clickData;
    }

    @Override
    public void run() {
        try {
            InboundCSVRow rowData;
            while ((rowData = queue.take()) != null) {

            }
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }
}
