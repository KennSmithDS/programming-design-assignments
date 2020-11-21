package sequentialSolution;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sequentialSolution.CSVReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CSVReaderTest {

    private CSVReader testReader1;
    private CSVReader testReader2;
    private String testFile;
    private static final int TEST_CLICKS = 3;
    private static final String[] STUDENT_HEADERS = {"module", "presentation", "student", "site", "date", "clicks"};

    @Before
    public void setUp() throws Exception {
        this.testFile = "anonymisedData/test.csv";
        this.testReader1 = new CSVReader(this.testFile);
        this.testReader2 = new CSVReader(this.testFile);
    }

    @Test (expected = FileNotFoundException.class)
    public void setupNoValidFile() throws FileNotFoundException {
        CSVReader failTest = new CSVReader("notavalid/path.csv");
    }

    @Test
    public void readCSVFile() throws IOException {
        HashMap<String, HashMap<String, Integer>> testAggData = this.testReader1.readCSVFile();
        int testTotalClicks = 0;
        for (String key : testAggData.keySet()) {
            HashMap<String, Integer> nested = testAggData.get(key);
            for (String subkey : nested.keySet()) {
                testTotalClicks += nested.get(subkey);
            }
        }
        Assert.assertEquals(TEST_CLICKS, testTotalClicks);
    }

    @Test
    public void testEquals() { Assert.assertEquals(this.testReader2, this.testReader1); }

    @Test
    public void testHashCode()  {
        Assert.assertEquals(this.testReader2.hashCode(), this.testReader1.hashCode());
    }

    @Test
    public void testToString() {
        String testString = "sequentialSolution.CSVReader{STUDENT_HEADERS=" + Arrays.toString(STUDENT_HEADERS) +
                ", csvFile='" + this.testFile + '\'' + '}';
        Assert.assertEquals(testString, this.testReader1.toString());
    }
}