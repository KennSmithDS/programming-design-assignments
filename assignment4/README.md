

```
java -jar message-generator -letter -letter_template letter-template.txt -output_dir letters -csv_file customer-data.csv
```

## Classes and Key Methods


- `JSONFileParser.java` **class**: Creates an object that takes a .json file (String) as input. 
  - `readFile()` **method**: Called on the JSONFile Parser object, which subsequently parses out the Grammar .json file (based on the specified regex) into a           HashMap with key String and value ArrayList of String info, which contains the file contents
  
- `Grammar.java` **class**: Creates a Grammar object from a JSONFileParser object, and we later use the Grammar objects for random sentence generation.
  - `Grammar(JSONFileParser file)` **method**: This is the first constructor, which takes an already created JSONFileParser, calls the `readFile()` method (which       parses out the file and assigns the HashMap to the info field of the Grammar object. Also, while reading the file, assigns the Grammar Title and Description         fields.
  - `Grammar(String file)` **method**: Same as the constructor above but takes in a String of the .json file name and creates the JSONFileParser object in the           constructor. Then, proceeds exactly the same way as the constructor above.
 

