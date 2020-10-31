import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.parser.ParseException;

public class Grammar {

  private String grammarTitle;
  private String grammarDesc;
  private HashMap<String, ArrayList> info;

  public Grammar(JSONFileParser file) throws IOException, ParseException, NoSuchJSONObjectException {
    this.info = file.readFile();
    this.grammarTitle = file.getTitle();
    this.grammarDesc = file.getDesc();
  }

  public String getGrammarTitle() {
    return this.grammarTitle;
  }

  public String getGrammarDesc() {
    return this.grammarDesc;
  }

  public HashMap<String, ArrayList> getInfo() {
    return this.info;
  }

  public static void main(String[] args)
      throws IOException, ParseException, NoSuchJSONObjectException {

    JSONFileParser poemTest = new JSONFileParser("poem_grammar.json");
    Grammar poemGrammar = new Grammar(poemTest);
    HashMap<String, ArrayList> testMap = poemGrammar.getInfo();
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

