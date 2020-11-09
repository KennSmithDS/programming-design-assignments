import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GrammarTest {

  //We will be using the sample.json and poem_grammar.json files in this project to perform
  //testing for the Grammar class.

  //Will be created using Constructor 1
  private Grammar sample;

  //Will be created using Constructor 2
  private Grammar poem;

  //Will attempt to be created using a nonexistent file to check for errors
  private Grammar fake;

  @Before
  public void setUp() throws IOException, ParseException {
    JSONFileParser sampleParser = new JSONFileParser("sample.json");
    this.sample = new Grammar(sampleParser);
    this.poem = new Grammar("poem_grammar.json");
  }

  @Test (expected = FileNotFoundException.class)
  public void failConstructor() throws IOException, ParseException {
    this.fake = new Grammar("nonexistent_file.json");
  }

  @Test
  public void getTitle() {
    Assert.assertEquals("A sample grammar", this.sample.getGrammarTitle());
    Assert.assertEquals("Poem Generator", this.poem.getGrammarTitle());
  }

  @Test
  public void getDescription() {
    Assert.assertEquals("A grammar that generates sample grammars.", sample.getGrammarDesc());
    Assert.assertEquals("A grammar that generates poems. ", poem.getGrammarDesc());
  }

  @Test
  public void getInfo() {
    HashMap<String, ArrayList<String>> sampleInfo = sample.getInfo();

    //Make sure the size is 3, and then all 3 keys are contained in the HashMap
    Assert.assertEquals(3, sampleInfo.size());
    Assert.assertTrue(sampleInfo.containsKey("start"));
    Assert.assertTrue(sampleInfo.containsKey("name"));
    Assert.assertTrue(sampleInfo.containsKey("lastName"));

    //Get each of the lists (values) associated with each key
    List<String> start = sampleInfo.get("start");
    List<String> name = sampleInfo.get("name");
    List<String> lastName = sampleInfo.get("lastName");

    //Make sure each of the lists is the right (expected) length and then check values

    //'start' arraylist
    Assert.assertEquals(1, start.size());
    Assert.assertEquals("hi <name>", start.get(0));

    //'name' arraylist
    Assert.assertEquals(1, name.size());
    Assert.assertEquals("Sally <lastName>", name.get(0));

    //'lastName' arraylist
    Assert.assertEquals(2, lastName.size());
    Assert.assertEquals("Smith", lastName.get(0));
    Assert.assertEquals("Jones", lastName.get(1));

  }

  @Test
  public void getInfoValue() {
    List<String> name = this.sample.getInfoValue("name");
    Assert.assertEquals(1, name.size());
    Assert.assertEquals("Sally <lastName>", name.get(0));
  }


}
