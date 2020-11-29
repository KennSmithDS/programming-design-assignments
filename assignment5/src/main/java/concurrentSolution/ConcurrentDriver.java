package concurrentSolution;

import sequentialSolution.NoSuchDirectoryException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class to represent the primary driver of the concurrent solution
 * Drives the instantiation of multiple producer and consumer threads for both reading and writing CSV files
 */
public class ConcurrentDriver {

    private static final InboundCSVRow readerPoison = new InboundCSVRow("thread", "killer", 0, 0, "", 0);
    private static final CSVFile writerPoison = null;
    private static final int QUEUE_BOUND = 10;
    private static final int N_PRODUCERS = 2;
    private static final int N_CONSUMERS = 2; //Runtime.getRuntime().availableProcessors();
    private static final int N_POISON_PER_PRODUCER = N_CONSUMERS / N_PRODUCERS;
    private static final int N_POISON_PILL_REMAIN = N_CONSUMERS % N_PRODUCERS;

    /**
     * Main method that will drive the producer-consumer design pattern for concurrent operation of CSV read/write
     * @param args String array of arguments passed in command line
     * @throws NoSuchDirectoryException custom exception error when no directory found
     * @throws InterruptedException default InterruptedException error for Threads
     */
    public static void main(String[] args) throws NoSuchDirectoryException, InterruptedException {
        String cliPath = "/Users/isidoraconic/Desktop/kendall_sample_files/studentVle_sample.csv"; //= args[0];
        String outputDir = "/Users/isidoraconic/Desktop/a5_output_files/";

        BlockingQueue<InboundCSVRow> readerQueue = new LinkedBlockingQueue<InboundCSVRow>(QUEUE_BOUND);
        BlockingQueue<CSVFile> writerQueue = new LinkedBlockingQueue<CSVFile>(QUEUE_BOUND);
        ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> aggStudentData = new ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>>();

        // reader threads go here
        new Thread(new CSVReaderProducer(cliPath, "test", readerQueue, readerPoison, N_CONSUMERS)).start();
        for (int i = 0; i < N_CONSUMERS; i++) {
            new Thread(new ClickAggregatorConsumer(readerQueue, aggStudentData, readerPoison)).start();
        }

        System.out.println(aggStudentData.get("AAA_2013J"));
        Thread.sleep(10000);

        // writer threads go here
        for(int i = 0; i < N_PRODUCERS; i++) {
            new Thread(new HashMapProducer(writerQueue, writerPoison, aggStudentData));
        }

        for(int j = 0; j < N_CONSUMERS; j++) {
            new Thread(new WriterConsumer(outputDir, writerQueue, writerPoison));
        }
    }
}
