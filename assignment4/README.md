

```
java -jar message-generator -letter -letter_template letter-template.txt -output_dir letters -csv_file customer-data.csv
```

## Classes and Key Methods


- `JSONFileParser.java` **class**: Creates an object that takes a .json file (String) as input. 
  - `readFile()` **method**: Called on the JSONFile Parser object, which subsequently parses out the Grammar .json file (based on the specified regex) into a           HashMap with key String and value ArrayList of String info, which contains the file contents
  
- `Grammar.java` **class**: Creates a Grammar object from a JSONFileParser object, and we later use the Grammar objects for random sentence generation.
  - `Grammar(JSONFileParser file)` **method**: This is the first constructor, which takes an already created JSONFileParser, calls the `readFile()` method (which       parses out the file and assigns the HashMap to the info field of the Grammar object. Also, while reading the file, assigns the Grammar Title and Description         fields.
  - `Grammar(String file)` **method**: Same as the constructor above but takes in a String of the .json file name and creates the JSONFileParser object in the           constructor. Then, proceeds exactly the same way as the constructor above.
  
- `SentenceGenerator.java` **class**: Class to represent a sentence generator, like a factory design but not quite. This class takes in a Grammar object (mentioned   above), and randomly builds a complete string sentence through recursive string replacement where it finds placeholders, denoted by '<>' brackets.
  - `buildSentence()` **method**: This is the main/primary method that generates a random sentence. It calls on a few helper (private) methods, which will be           outlined below.
  - `getRandomGrammarElement(String key)` **private helper method**: Gets a random String from Grammar ArrayList. The ArrayList is selected based on the passed key     (String) which always begins with <start>, and then will recursively be called to get other random elements in the SentenceBuilder as necessary.
  - `buildGrammarStack(String input)` **private helper method**: Builds a Stack of String tokens from a Grammar element. Stack is in reverse order so we can work       with tokens from front to end (in sentence order).
  - `recursiveStringReplace(Stack<String> grammarStack)` **private helper method**: Recursively steps into the Stack of grammar Strings. The method pops elements       from Stack to check if placeholder match. If placeholders match, swap the placeholder with a random choice, push back to the Stack and recurse down. Otherwise,     append first element of Stack to complete sentence, and recurse down with remaining Stack. 
  
- `UserInterface.java` **class**: This class serves as a front-end interface for a user. It creates all the Grammar objects, and then from their grammarTitles         creates/prints a menu. The class then takes in input from the user to allow them to select a grammar and then generates and prints it.
  - `menuCommand()` **method**: This method starts off the entire program (called in the `main()` method. Prints the menu (and all the Grammar options provided in       the directoory (called in command line). After the menu is printed, it takes input from the user (in the method explained below) and continues to get input         until the user quits the program (by calling 'q').
  - `readInput()` **private helper method**: Helper method that uses a buffered reader and reads the next line passed by the user (i.e. their command). The method       will keep reading until it gets a valid input (checked by the checkInput method). It also prompts the user to give a valid input if their input isn't valid         (prints to console).
  - `checkInput(String input)` **private helper method**: Helper method for `readInput()`. It will check if the read input is valid--if it is either a number within     the range of offered grammars, or simply 'q' if we want to quit the program. 
  - `handleInput(String input)` **method**: Method that handles the selected Grammar choice (or 'q' to quit) and constructs the sentence. Once it is done, it prints     the sentence to the console, and then prompts the user for a new input, and reads it.`
  - `main()` **method**: Entry point into the program. It is called with a command line argument, which is the directory where the grammar .json files are located.     With this, it creates a UserInterface object (or throws an error if the directory is not valid). The program is also terminated if the user doesn't pass a           command line argument (directory). Reads all the .json grammar files, creates Grammar objects, and adds them to the Grammars list. Then, prompts the user with       menu options and prints appropriate Grammar sentences.

