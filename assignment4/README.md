# Assignment 3 - Insurance Company Email Automation

In this assignment, we are tasked with creating a application entry point for an analyst or someone who works at an insurance company.  There has been a data breach, and the insurance company has to send out hundreds or thousands of messages to their customers.  The messages can come in two formats, either email or letter, using those respective templates.  The goal of the application is to provide a command line environment for the employee to automatically generate an email or letter for every customer in the database.

## Setup

There is one core dependency for this Java application to work, and that is a library that comes from the Apache Commons:

```
https://mvnrepository.com/artifact/commons-cli/commons-cli/1.4
```

You will need to make sure that this library is downloaded and available in your global java library for the application to work if running the application within an IDE or CLI.

## Usage

In order to run this application, you simply need to enter one command line of arguments:

```
java -jar message-generator.jar -letter -letter_template templates/letter-template.txt -output_dir letters -csv_file data/insurance-company-members.csv
```

In this example above, we are generating a letter for each coustomer from the "/templates" folder using the letter-template.txt template file, and the messages are written to the "/letters" output directory.  This folder will be created if it does not already exist.  Lastly the customer data is retreived from a CSV data file called insurance-comany-members.csv which is stored in the folder path "/data"

```
-email                    Only generate email messages
-email_template your/file/path    Accept a filename that holds the email template
                          **Required if -email is used**
-letter                   **Only generate letter messages
-letter_template your/file/path   Accept a filename that holds the letter template
                          **Required if -letter is used**
```

**The email and letter types cannot be crossed or this will throw an error in the system.  If you want an email, you must provide an email template argument, and same for letters.**

## Examples

Example command line arguments for generating emails from customer data:

```
java -jar message-generator.jar -email -email_template email-template.txt -output_dir emails -csv_file customer-data.csv
```

Example command line arguments for generating letters from customer data:

```
java -jar message-generator -letter -letter_template letter-template.txt -output_dir letters -csv_file customer-data.csv
```