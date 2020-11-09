import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserInterfaceTest {

    private UserInterface testUI;
    private Grammar testGrammar;
    private static final String jsonPath = "./json/";

    @Before
    public void setUp() throws Exception {
        this.testUI = new UserInterface();
        this.testGrammar = new Grammar(jsonPath + "sample.json");
    }

    @Test
    public void getSetDirectory_ValidPath() throws NoSuchDirectoryException {
        this.testUI.setDirectory(jsonPath);
        Assert.assertEquals(jsonPath, this.testUI.getDirectory());
    }

    @Test (expected = NoSuchDirectoryException.class)
    public void setDirectory_InvalidPath() throws NoSuchDirectoryException  {
        this.testUI.setDirectory("C:/Tests/and/then/some/");
    }

    @Test
    public void addGrammar() {
        this.testUI.addGrammar(this.testGrammar);
    }

    @Test
    public void menuCommand() {

    }

    @Test
    public void handleInput() {
    }

    @Test
    public void testToString() {
    }

    @Test
    public void testEquals() {
    }

    @Test
    public void testHashCode() {
    }

    @Test
    public void main() {
    }
}

//    @Test
//    public void systemExitWithSelectedStatusCode0() {
//        exit.expectSystemExitWithStatus(0);
//        //the code under test, which calls System.exit(0);
//    }