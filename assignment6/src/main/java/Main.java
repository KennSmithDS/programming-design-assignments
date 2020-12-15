import Communications.Communication;
import Communications.ConnectMessage;
import Communications.InvalidMessageException;
import java.util.Scanner;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

  public static void main(String[] args) throws InvalidMessageException, IOException, ParseException, NoSuchGrammarTypeException {

//    String name = "isidora";
//    byte[] nameByte = name.getBytes();
//    int size = name.length();
//
//    ConnectMessage msg = new ConnectMessage(size, nameByte);
//
//    if(msg instanceof Communications.ConnectMessage) {
//      System.out.println("Hello!");
//    }
    //System.out.println(msg.getClass());

    String insult = new SentenceGenerator(new Grammar(new JSONFileParser("./lib/insult_grammar.json")), null).buildSentence();
    System.out.println(insult);
  }

  public static String readShit() {
    Scanner console = new Scanner(System.in);
    String input = console.nextLine();
    return input;
  }

}
