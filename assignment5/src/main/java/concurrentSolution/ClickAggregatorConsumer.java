package concurrentSolution;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class to represent a Consumer that takes student click data CSV rows from BlockingQueue
 * Since it is designed to work in a Thread, it needs to implement Runnable and override run()
 * The class just takes parsed rows of a CSV file placed into a Blocking Queue
 * Then the consumer aggregates the clicks for each unique module and presentation and date
 */
public class ClickAggregatorConsumer implements Runnable {

    private final BlockingQueue<InboundCSVRow> queue;
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> aggStudentData;
    private final InboundCSVRow poison;

    /**
     * Constructor method for ClickAggregatorConsumer class
     * @param queue BlockingQueue from Driver that the CSV rows will be stored in
     * @param aggStudentData ConcurrentHashMap object to aggregate the student clicks
     * @param poison CSV row poison pill that will kill each consumer thread
     */
    ClickAggregatorConsumer(BlockingQueue<InboundCSVRow> queue,
                            ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> aggStudentData,
                            InboundCSVRow poison) {
        this.queue = queue;
        this.aggStudentData = aggStudentData;
        this.poison = poison;
    }

    /**
     * Method to write student clicks into the ConcurrentHashMap
     * Checks if codeKey (module + presentation) is present and date
     * Then puts new value or updates value of existing value
     * @param codeKey concatenated String of module and presentation codes
     * @param date String of the date since days of the assessment
     * @param clicks Integer clicks by the student
     */
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

    /**
     * Override of Runnable.run() method to take each parsed row from the BlockingQueue
     * The student click data is aggregated with the writeToHash method
     * When the Thread finds a poison pill, it will terminate
     */
    @Override
    public void run() {
        try {
            InboundCSVRow csvRow;
            while (!(csvRow = queue.take()).getCodeKey().equals(poison.getCodeKey())) {
                writeToHash(csvRow.getCodeKey(), csvRow.getDate(), csvRow.getClicks());
                System.out.println("Just wrote row: " + csvRow.toString() + " to the hashmap!");
            }
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Override of default equals() method
     * @param o object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClickAggregatorConsumer that = (ClickAggregatorConsumer) o;
        return Objects.equals(queue, that.queue) &&
                Objects.equals(aggStudentData, that.aggStudentData) &&
                Objects.equals(poison, that.poison);
    }

    /**
     * Override of default hashCode() method
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(queue, aggStudentData, poison);
    }

    /**
     * Override of default toString() method
     * @return String
     */
    @Override
    public String toString() {
        return "ClickAggregatorConsumer{" +
                "queue=" + queue +
                ", aggStudentData=" + aggStudentData +
                ", poison=" + poison +
                '}';
    }
}
