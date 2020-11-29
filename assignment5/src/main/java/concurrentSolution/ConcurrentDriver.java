package concurrentSolution;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import sequentialSolution.NoSuchDirectoryException;
import sequentialSolution.NullCommandLineArgument;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class to represent the primary driver of the concurrent solution
 * Drives the instantiation of multiple producer and consumer threads for both reading and writing CSV files
 */
public class ConcurrentDriver {

    private static final InboundCSVRow readerPoison = new InboundCSVRow("thread", "killer", 0, 0, "", 0);
    private static final CSVFile writerPoison = new CSVFile("poison");
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
    public static void main(String[] args) throws NoSuchDirectoryException, InterruptedException, NullCommandLineArgument {
        if (args.length < 1) {
            throw new NullCommandLineArgument("The command line argument was null/empty. Please provide a valid folder path to CSV data.");
        } else if (args.length == 1) {
            String cliPath = args[0];
            ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> aggStudentData = readStudentData(cliPath);
            writeMultipleFiles(cliPath, aggStudentData);
        } else {
            String cliPath = args[0];
            Integer threshold = Integer.parseInt(args[1]); // need to check data type or throw error
            ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> aggStudentData = readStudentData(cliPath);
            writeSingleThresholdFile(cliPath, threshold, aggStudentData);
        }
    }

    public static ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> readStudentData(String path) throws NoSuchDirectoryException, InterruptedException {
        BlockingQueue<InboundCSVRow> readerQueue = new LinkedBlockingQueue<InboundCSVRow>(QUEUE_BOUND);
        ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> aggStudentData = new ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>>();

        // reader threads go here
        new Thread(new CSVReaderProducer(path, "test", readerQueue, readerPoison, N_CONSUMERS)).start();
        for (int i = 0; i < N_CONSUMERS; i++) {
            new Thread(new ClickAggregatorConsumer(readerQueue, aggStudentData, readerPoison)).start();
        }

        // sleep between CSV reader and writer Thread workflows
        Thread.sleep(1000);
        return aggStudentData;
    }

    public static void writeMultipleFiles(String path, ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> studentData) throws NoSuchDirectoryException {
        BlockingQueue<CSVFile> writerQueue = new LinkedBlockingQueue<CSVFile>(QUEUE_BOUND);

        //List of keys for the writer producer and consumer to use
        CopyOnWriteArrayList<String> keyList = new CopyOnWriteArrayList<>(studentData.keySet());
//        for(String key : studentData.keySet()) {
//            keyList.add(key);
//        }

        //Writer threads go here
        for(int i = 0; i < N_PRODUCERS; i++) {
            new Thread(new HashMapProducer(writerQueue, writerPoison, studentData, N_CONSUMERS, keyList)).start();
        }

        for(int j = 0; j < N_CONSUMERS; j++) {
            new Thread(new WriterConsumer(path, writerQueue, writerPoison)).start();
        }
    }

    public static void writeSingleThresholdFile(String path, Integer threshold, ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> studentData) {

    }
}