# Assignment 5 - OULAD Student Click Aggregator

In this assignment, we are tasked to read a very large dataset from the Open University Learning Analytics Dataset (OULAD) which is collected from students in online courses in the UK. The dataset we worked with specifically is studentVle.csv which has over 10 million rows of data, containing module codes and presentation codes, the student and site IDs, as well as the date in days from assessment, and the sum total of clicks by a given student on that date. The tasks was to read this file (.5GB) with both a sequential and concurrent solution. Given the file is 10 million rows, the sequential solution will be very slow, while the concurrent solution should take seconds.  The goal ultimately is to take the large file and aggregate the clicks on the module/presentation codes and date. 

The sequential solution design is very basic and linear, while the concurrent solution required the use of multiple producer/consumer pairs to concurrently read, parse, aggregate, fetch, and write to CSV file output. With the concurrent solution, we were asked to provide two version that output different files.  The first concurrent solution does the exact same thing as the sequential solution, which writes a different file (22 in total) for each unique combination of module and presentation codes. The second concurrent solution takes in a threshold value after the folder path that is used to filter the aggregated rows of data to days with clicks above the threshold. For example, if 10000 is passed as the second agrument, then all codes and days with clicks equal to or over 10000 will be written into a single CSV file.

## Setup

There are two core dependencies for this Java application to work, and both libraries come from the Apache Commons:

```
CSVParser -> https://mvnrepository.com/artifact/org.apache.commons/commons-csv
ThreadUtils -> https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
```

You will need to make sure that these libraries are downloaded and available in your global java library for the application to work if running the application within an IDE or CLI. If you are running using the compiled .jar file below with command line arguments, then it should run just fine.

**Make sure you are in assignment 5 folder when running the program in terminal!**

## Usage

In order to run this application, you simply need to enter one commandline with either one or two arguments, the first which is the full or implicit directory path to a folder containing CSV data files, and the second is a threshold:

```
Sequential Solution:
java -jar sequentialSolution.jar ./anonymisedData

Concurrent Solution 1:
java -jar concurrentSolution.jar ./anonymisedData

Concurrent Solution 2:
java -jar concurrentSolution.jar ./anonymisedData 10000
```

The program will then execute by itself and the user will quickly see files being created in the same input folder passed into the command line argument.

## Examples

Example command line arguments for generating aggregated CSV files from studnet data:

```
Example 1:
java -jar sequentialSolution.jar ./anonymisedData
The CSV file is: ./anonymisedData/studentVle.csv
[Program Finishes - there are now 22 CSV files in the folder]

Example 2:
java -jar concurrentSolution.jar ./anonymisedData 10000
Made the writer
Started the writer!
Closed the buffered writer!
[Program Finishes - there is now a new CSV file activity-10000.csv in the folder]
```

## Classes and Methods
#### Classes are in order of low-level to high level

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


## Assumptions and Edge Cases

1) We assumed that the user is running the application with a valid folder path to where only JSON files exist, otherwise the application will not execute.
2) Correspondingly, we assume that the grammar JSON files are all formatted the same, in that they contain a **grammarTitle**, **grammarDescription** and a **start** so that the program will be able to begin the recursive calls into the sub-grammar types, and store the respective properties in the Grammar class object.
3) The grammar files are assumed to be constructed in a way that they will have valid sub-types to reference. If a grammar type is not found, the application will notify the user that that path was not valid, but give them the option to try another building another sentence.
4) One special edge case we handle for is if the application throws a stackoverflow error. This would occur when a grammar type references itself or there is a loop between two grammar types, etc. For example, if the "start" was "<verb>", and the only verb in the "verb" grammar sub-type was "<verb>", this would create an infinite recusive call to the sub-type.
5) We also assume that the user will pass a valid input for the grammar files provided in the menu. For example, if there are four grammar JSON files, the user should input 1, 2, 3, or 4.  If they pass '5', which is out of index, then a helper method will return a false boolean value and prompt the user to try again. Likewise, if they pass 'a' or some other letter/character, then the helper will again pass a false boolean value and prompt the user to try again.
6) In order to test the main() method of the 'UserInterface.java' class and simulate user input, we had to make a modification to handle a second argument in the command line. If only the required first argument for the JSON folder path is provided, the application will run as normal. However, if 'q' is passed as a second paramter, then the application will enter into test mode, and quit immediately.  This call however allowed for some successful testing of the UserInterface class methods that take in user input.
7) Similarly to the above, 'SentenceGenerator.java' needed a paramter in the constructor method for the class to store a random seed or null.  If null is passed, then the program will pull a truly random result with different seeds each time. However, for testing purposes, if the consturctor includes a Long value for the seed, then the application will use a fixed random seed to call the same grammar list items using same random seed each time. This ensures reproduceability and stable testing.
