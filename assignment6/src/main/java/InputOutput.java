import java.util.Scanner;

public class InputOutput {

  private final Scanner scanner;

  public InputOutput()
  {
    //the external exposed default constructor
    //would use constructor-chaining to pass an instance of Scanner.

    this(new Scanner(System.in));
  }

  //declare a package level constructor that would be visible only to the test class.
  //It is a good practice to have a class and it's test within the same     package.
  InputOutput(Scanner scanner)
  {
    this.scanner  = scanner;
  }

  public String getInput() {

    return scanner.nextLine();
  }
}