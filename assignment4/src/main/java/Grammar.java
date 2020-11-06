import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.parser.ParseException;

/**
 * Grammar Class
 * This creates a Grammar object that contains all the data from a .json file
 * The .json file is read/handled by the JSONFileParser class which helps creater the Grammar object
 */
public class Grammar {

  private String grammarTitle;
  private String grammarDesc;
  private HashMap<String, ArrayList<String>> info;

  /**
   * Grammar object constructor 1
   * Reads the JSONFileParser object and then gets the grammarTitle and grammarDesc
   * @param file (JSONFileParser object)
   * @throws IOException
   * @throws ParseException
   */
  public Grammar(JSONFileParser file) throws IOException, ParseException {
    this.info = file.readFile();
    this.grammarTitle = file.getTitle();
    this.grammarDesc = file.getDesc();
    System.out.println("Grammar object with title: " + this.grammarTitle + " made.");
  }

  /**
   * Grammar object constructor 2
   * Takes in a String fileName and then constructs the JSONFileParser object
   * Then, does the same as Constructor 2: Reads the JSONFileParser object
   * And gets the grammarTitle and grammarDesc
   * @param fileName (String)
   * @throws IOException
   * @throws ParseException
   */
  public Grammar(String fileName) throws IOException, ParseException {
    JSONFileParser file = new JSONFileParser(fileName);
    this.info = file.readFile();
    this.grammarTitle = file.getTitle();
    this.grammarDesc = file.getDesc();
    System.out.println("Grammar object with title: " + this.grammarTitle + " made.");
  }

  /**
   * Getter method for the grammarTitle
   * @return String grammarTitle
   */
  public String getGrammarTitle() {
    return this.grammarTitle;
  }

  /**
   * Getter method for grammarDesc
   * @return String grammarDesc
   */
  public String getGrammarDesc() {
    return this.grammarDesc;
  }

  /**
   * Getter method for info
   * @return HashMap<String, ArrayList<String>> info
   */
  public HashMap<String, ArrayList<String>> getInfo() {
    return this.info;
  }

  /**
   * Getter method for a specific ArrayList<String> within the info HashMap
   * @param key (String)
   * @return ArrayList<String> associated with the given key
   */
  public ArrayList<String> getInfoValue(String key) {
    return this.info.get(key);
  }

  public static void main(String[] args)
      throws IOException, ParseException {

    JSONFileParser poemTest = new JSONFileParser("poem_grammar.json");
    Grammar poemGrammar = new Grammar(poemTest);
    HashMap<String, ArrayList<String>> testMap = poemGrammar.getInfo();
    String grammarTitle = poemTest.getTitle();
    String grammarDesc = poemTest.getDesc();

    System.out.println("The title is: "+grammarTitle);
    System.out.println("The description is: " +grammarDesc);
    System.out.println();

    for(String key : testMap.keySet()) {
      System.out.println("The key is: " + key);
      ArrayList<String> keyValues = testMap.get(key);
      for(String value : keyValues) {
        System.out.println(value);
      }
      System.out.println();
    }
  }

}

