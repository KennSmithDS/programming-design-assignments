import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
//import org.json.simple.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;


public class JSONFileParser {

  private String jsonFile;

  public JSONFileParser(String fileName) throws FileNotFoundException {
    if (!(new File(fileName).exists())) {
      throw new FileNotFoundException("The specified template file does not exist. "
          + "Please enter an existing file.");
    } else {
      this.jsonFile = fileName;
    }
  }

  public void readFile() throws IOException, ParseException {

    //Setting up the JSON parser
    Object obj = new JSONParser().parse(new FileReader(this.jsonFile));
    JSONObject parser = (JSONObject) obj;

    //Getting the grammarTitle and grammarDesc (if they exist)
    String grammarTitle; //= (String) parser.get("grammarTitle");
    String grammarDesc; //= (String) parser.get("grammarDesc");

    //Get all the keys and removing
    Set keys = parser.keySet();

    if(keys.contains("grammarTitle")) {
      grammarTitle = (String) parser.get("grammarTitle");
      keys.remove("grammarTitle");
    }

    if(keys.contains("grammarDesc")) {
      grammarDesc = (String) parser.get("grammarDesc");
      keys.remove("grammarDesc");
    }

    //Making the hashmap to store all the parsed JSON file info
    HashMap<String, ArrayList> info = new HashMap<>();

    for(Object s : keys) {

      //Getting one of the keys
      String key = (String) s;

      //ArrayList to store all the key values
      ArrayList<String> keyValues = new ArrayList<>();
      System.out.println(key);

      JSONArray test = (JSONArray) parser.get(key);
      Iterator<JSONObject> iterator = test.iterator();
      while(iterator.hasNext()){
        //String obj1 = iterator.next().toString();
        System.out.println(iterator.next());

      }
      System.out.println();
      System.out.println();

    }

  }

  public static void main(String[] args) throws IOException, ParseException {
    JSONFileParser test = new JSONFileParser("poem_grammar.json");
    test.readFile();
  }


}
