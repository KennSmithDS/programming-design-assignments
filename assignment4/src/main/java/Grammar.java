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

}

