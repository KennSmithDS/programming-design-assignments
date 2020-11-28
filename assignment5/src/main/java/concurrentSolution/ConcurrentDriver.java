package concurrentSolution;

import sequentialSolution.NoSuchDirectoryException;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class ConcurrentDriver {

    private static final InboundCSVRow readerPoison = null;
    private static final ConcurrentHashMap<String, Integer> writerPoison = null;
    private static final int QUEUE_BOUND = 10;

    public static void main(String[] args) throws NoSuchDirectoryException {
        String path = args[0];

        BlockingQueue<InboundCSVRow> readerQueue = new LinkedBlockingQueue<>(QUEUE_BOUND);
        BlockingQueue<ConcurrentHashMap<String, Integer>> writerQueue = new LinkedBlockingQueue<>(QUEUE_BOUND);
        ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> aggStudentData = new ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>>();

        // reader threads go here
//        new Thread(new CSVReaderProducer(path, "test", readerQueue, readerPoison)).start();
//        new Thread(new ClickAggregatorConsumer(readerQueue, aggStudentData, readerPoison)).start();

        // writer threads go here
    }
}
