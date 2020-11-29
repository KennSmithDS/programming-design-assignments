package concurrentSolution;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
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
    public static void main(String[] args) throws NoSuchDirectoryException, InterruptedException {
        String cliPath = "/Users/isidoraconic/Desktop/kendall_sample_files"; //= args[0];
        String outputDir = "/Users/isidoraconic/Desktop/a5_output_files/";

        BlockingQueue<InboundCSVRow> readerQueue = new LinkedBlockingQueue<InboundCSVRow>(QUEUE_BOUND);
        BlockingQueue<CSVFile> writerQueue = new LinkedBlockingQueue<CSVFile>(QUEUE_BOUND);
        ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> aggStudentData = new ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>>();

        // reader threads go here
        new Thread(new CSVReaderProducer(cliPath, "test", readerQueue, readerPoison, N_CONSUMERS)).start();
        for (int i = 0; i < N_CONSUMERS; i++) {
            new Thread(new ClickAggregatorConsumer(readerQueue, aggStudentData, readerPoison)).start();
        }


        Thread.sleep(10000);
        System.out.println();
        System.out.println("The size of the hashmap is: " + aggStudentData.size());
        System.out.println();

        // writer threads go here
        for(int i = 0; i < N_PRODUCERS; i++) {
            new Thread(new HashMapProducer(writerQueue, writerPoison, aggStudentData, N_CONSUMERS)).start();
        }

        for(int j = 0; j < N_CONSUMERS; j++) {
            new Thread(new WriterConsumer(outputDir, writerQueue, writerPoison)).start();
        }


        /*

        for(Map.Entry outerKey : aggStudentData.entrySet()) {

            StringBuilder sb1 = new StringBuilder();
            sb1.append(outerKey);
            String codeKey = sb1.toString();
            outputFile = new CSVFile(codeKey);

            for(Map.Entry innerKey : this.map.get(outerKey).entrySet()) {
                CopyOnWriteArrayList<String> row = new CopyOnWriteArrayList<>();
                StringBuilder sb2 = new StringBuilder();
                sb2.append(innerKey);
                String date = sb2.toString();
                sb2 = new StringBuilder();
                sb2.append(this.map.get(outerKey).get(innerKey));
                String clicks = sb2.toString();
                row.add(date);
                row.add(clicks);
                outputFile.addRow(row);
            }
            return outputFile;

            //Should I break here? I.e. only does one element at a time?
            //Will this essentially block every single thread other than one from doing this bc it is
            //a concurrent hashmap?
        }

         */
    }
}
