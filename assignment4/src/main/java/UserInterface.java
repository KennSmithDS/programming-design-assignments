import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

  public static void main(String[] args) {


  }

}
