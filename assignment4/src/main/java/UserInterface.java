import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.parser.ParseException;

public class UserInterface {

  private String directory;
  private List<Grammar> grammarList;

  public UserInterface() {
    this.directory = null;
    this.grammarList = new ArrayList<>();
  }

  public void setDirectory(String directory) throws NoSuchDirectoryException {
    //Check if the output directory exists, if not, throw NoSuchDirectoryException
    if (!(new File(directory).exists())) {
      throw new NoSuchDirectoryException("The specified directory does not exist. "
          + "Please enter an existing directory.");
    } else {
      this.directory = directory;
    }
  }

  public String getDirectory() {
    return this.directory;
  }

  public void addGrammar(Grammar grammar) {
    this.grammarList.add(grammar);
  }

  //Is it poor form to have a method that prints?
  public String menuCommand() throws IOException {
    System.out.println("The following grammars are available :");
    int counter = 1;
    for(Grammar g : this.grammarList) {
      System.out.println("[" + counter + "] " + g.getGrammarTitle());
      counter++;
    }
    System.out.println();
    System.out.println("Please enter a number corresponding to one of the options above, or 'q' to quit.");
    return readInput();
  }

  private String readInput() throws IOException {
    //Get the option selected and make sure it is in the correct format
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String input = br.readLine();
    while(input == null || !checkInput(input)){
      System.out.println();
      System.out.println("Please enter a VALID output.");
      System.out.println("Enter a number corresponding to one of the options above, or 'q' to quit.");
      input = br.readLine();
    }
    return input;
  }

  public void handleInput(String input) throws IOException {
    int grammarChoice = 0;
    while(!input.equals("q")) {
      grammarChoice = Integer.parseInt(input)-1;
      SentenceGenerator sentenceGen = new SentenceGenerator(this.grammarList.get(grammarChoice));
      String sentence = sentenceGen.buildSentence();
      System.out.println(sentence);
      System.out.println();
      System.out.println("Enter a number corresponding to one of the menu options for another grammar, or 'q' to quit.");
      input = readInput();
    }
  }

  private boolean checkInput(String input) {
    int choice = 0;

    //First, we check if the string is a char = q
    if(input.charAt(0) == 'q' && input.length() == 1) {
      return true;
    }

    //If the input is not q, it should be an int
    //We try to parse the string to int, if it fails, we know the input is not valid (return false)
    try {
      choice = Integer.parseInt(input);
    } catch (NumberFormatException e) {
      return false;
    }

    //If it a valid integer, we just check if it is within the range of number of grammars given
    return choice > 0 && choice <= this.grammarList.size();
  }

  public int lineReader() throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int choice = Integer.parseInt(br.readLine());
    return choice;
  }


  public static void main(String[] args)
      throws NoSuchDirectoryException, IOException, ParseException {

    UserInterface ui = new UserInterface(); //  /Users/isidoraconic/Desktop/json_files/
    try {
      ui.setDirectory(args[0]);
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("You did not provide any directory to start the program." +
          " Please provide a valid/existing directory with .json files.");
      System.exit(0);
    }

    //Getting all files in the specified directory, and making Grammar objects out of them
    //Inspired by: https://stackoverflow.com/questions/5694385/getting-the-filenames-of-all-files-in-a-folder
    File folder = new File(ui.getDirectory());
    File[] listOfFiles = folder.listFiles();
    for(int i = 0; i < listOfFiles.length; i++) {
      String filePath = ui.getDirectory() + "/" + listOfFiles[i].getName();
      Grammar add = new Grammar(filePath);
      ui.addGrammar(add);
    }

    String input = ui.menuCommand();
    ui.handleInput(input);
  }

}
