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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;


public class JSONFileParser {

  private String jsonFile;
  private String splitBy;
  private final static String LINE_SPLIT = "(?<=\")[^,;](.*?)(?=\")";

  public JSONFileParser(String fileName) throws FileNotFoundException {
    if (!(new File(fileName).exists())) {
      throw new FileNotFoundException("The specified template file does not exist. "
          + "Please enter an existing file.");
    } else {
      this.jsonFile = fileName;
    }
    this.splitBy = LINE_SPLIT;
  }

  public JSONFileParser(String fileName, String splitBy) throws FileNotFoundException {
    if (!(new File(fileName).exists())) {
      throw new FileNotFoundException("The specified template file does not exist. "
          + "Please enter an existing file.");
    } else {
      this.jsonFile = fileName;
    }
    this.splitBy = splitBy;
  }

  public HashMap<String, ArrayList> readFile() throws IOException, ParseException {

    //Setting up the JSON parser
    Object obj = new JSONParser().parse(new FileReader(this.jsonFile));
    JSONObject parser = (JSONObject) obj;

    //Making the hashmap to store all the parsed JSON file info
    HashMap<String, ArrayList> info = new HashMap<>();

    //Get all the keys and removing
    Set keys = parser.keySet();

    //We dont want grammarTitle in the hashmap if it exists in the file
    //We have a different method for getting that
    if(keys.contains("grammarTitle")) {
      keys.remove("grammarTitle");
    }

    //We dont want grammarDesc in the hashmap if it exists in the file
    //We have a different method for getting that
    if(keys.contains("grammarDesc")) {
      keys.remove("grammarDesc");
    }

    //now, we iterate through all the rest of the keys
    //get the values corresponding to the list by parsing them and adding to a list
    for(Object s : keys) {

      //Getting one of the keys
      String key = (String) s;

      //Get the key as a STRING
      String line = parser.get(key).toString();

      //ArrayList to store all the key values, created by splitting the values in the string
      ArrayList<String> keyValues = patternMatch(line);

      //Add the arraylist and corresponding key to the hashmap
      info.put(key, keyValues);
    }

    return info;
  }

  private ArrayList<String> patternMatch(String inputString) {
    ArrayList<String> parsedString = new ArrayList<>();
    Pattern pattern = Pattern.compile(this.splitBy);
    Matcher matcher = pattern.matcher(inputString);
    while (matcher.find()) {
      parsedString.add(matcher.group());
    }
    return parsedString;
  }

  public String getTitle() throws IOException, ParseException, NoSuchJSONObjectException {

    //Setting up the JSON parser
    Object obj = new JSONParser().parse(new FileReader(this.jsonFile));
    JSONObject parser = (JSONObject) obj;

    //Getting the grammarTitle (if it exists)
    String grammarTitle = "";

    //Get all the keys so that we can check if such a key exists
    Set keys = parser.keySet();

    //if the grammarTitle exists, add it to a list and as a key into hashmap
    //then, remove it from the JSON object
    if(keys.contains("grammarTitle")) {
      grammarTitle = (String) parser.get("grammarTitle");
      keys.remove("grammarTitle");
    } else {
      throw new NoSuchJSONObjectException("This JSON file doesn't have a 'grammarTitle'.");
    }
    return grammarTitle;
  }

  public String getDesc() throws IOException, ParseException, NoSuchJSONObjectException {

    //Setting up the JSON parser
    Object obj = new JSONParser().parse(new FileReader(this.jsonFile));
    JSONObject parser = (JSONObject) obj;

    //Getting the grammarDesc (if it exists)
    String grammarDesc = "";

    //Get all the keys so that we can check if such a key exists
    Set keys = parser.keySet();

    //if the grammarTitle exists, add it to a list and as a key into hashmap
    //then, remove it from the JSON object
    if(keys.contains("grammarTitle")) {
      grammarDesc = (String) parser.get("grammarTitle");
      keys.remove("grammarTitle");
    } else {
      throw new NoSuchJSONObjectException("This JSON file doesn't have a 'grammarTitle'.");
    }
    return grammarDesc;


  }

  public static void main(String[] args) throws IOException, ParseException {
    JSONFileParser test = new JSONFileParser("poem_grammar.json");
    HashMap<String, ArrayList> testMap = test.readFile();

    for(String key : testMap.keySet()) {
      System.out.println("The key is: " + key);
      ArrayList<String> keyValues = testMap.get(key);
      for(String value : keyValues) {
        System.out.println(value);
      }
      System.out.println();
      System.out.println();
    }
  }


}

