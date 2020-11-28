package concurrentSolution;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;

public class ClickAggregatorConsumer implements Runnable {

    private BlockingDeque<InboundCSVRow> queue;
    private ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> aggStudentData;
    private InboundCSVRow poison;

    ClickAggregatorConsumer(BlockingDeque<InboundCSVRow> queue,
                            ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> aggStudentData,
                            InboundCSVRow poison) {
        this.queue = queue;
        this.aggStudentData = aggStudentData;
        this.poison = poison;
    }

    public void writeToHash(String codeKey, String date, Integer clicks) {
        // module and presentation code exists in HashMap
        if (aggStudentData.containsKey(codeKey)) {
            // date exists in HashMap
            if (aggStudentData.get(codeKey).containsKey(date)) {
                Integer storedClicks = aggStudentData.get(codeKey).get(date);
                Integer newClicks = storedClicks + clicks;
                aggStudentData.get(codeKey).put(date, newClicks);
            } else { //date does not exist in HashMap
                aggStudentData.get(codeKey).put(date, clicks);
            }
        } else { // module and presentation code don't exist in HashMap
            aggStudentData.put(codeKey, new ConcurrentHashMap<>());
            aggStudentData.get(codeKey).put(date, clicks);
        }
    }
    
    @Override
    public void run() {
        try {
            InboundCSVRow csvRow;
            while ((csvRow = queue.take()) != poison) {
                writeToHash(csvRow.getCodeKey(), csvRow.getDate(), csvRow.getClicks());
            }
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClickAggregatorConsumer that = (ClickAggregatorConsumer) o;
        return Objects.equals(queue, that.queue) &&
                Objects.equals(aggStudentData, that.aggStudentData) &&
                Objects.equals(poison, that.poison);
    }

    @Override
    public int hashCode() {
        return Objects.hash(queue, aggStudentData, poison);
    }

    @Override
    public String toString() {
        return "ClickAggregatorConsumer{" +
                "queue=" + queue +
                ", aggStudentData=" + aggStudentData +
                ", poison=" + poison +
                '}';
    }
}
