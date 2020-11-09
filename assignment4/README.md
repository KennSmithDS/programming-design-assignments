

```
java -jar message-generator -letter -letter_template letter-template.txt -output_dir letters -csv_file customer-data.csv
```

## Classes and Key Methods


- `JSONFileParser.java` : This class creates an object that takes a .json file (String) as input. 
  - `readFile()` method: called on the JSONFile Parser object, which subsequently parses out the Grammar .json file (based on the specified regex) into a HashMap       with key String and value ArrayList of String info, which contains the file contents
 

