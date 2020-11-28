package concurrentSolution;

import java.util.HashMap;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;

public class ClickAggregatorConsumer implements Runnable {

    private BlockingDeque<InboundCSVRow> queue;
    private ConcurrentHashMap<String, ConcurrentHashMap<Integer, Integer>> aggStudentData;

    ClickAggregatorConsumer(BlockingDeque<InboundCSVRow> queue, ConcurrentHashMap<String, ConcurrentHashMap<Integer, Integer>> aggStudentData) {
        this.queue = queue;
        this.aggStudentData = aggStudentData;
    }

    public void writeToHash(String codeKey, Integer date, Integer clicks) {
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
            while ((csvRow = queue.take()) != null) {
                writeToHash(csvRow.getCodeKey(), csvRow.getDate(), csvRow.getDate());
            }
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }
}
