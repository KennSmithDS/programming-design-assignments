package Communications;

import java.nio.charset.StandardCharsets;

/**
 * Class that is a subtype of Message to represent SEND_INSULT (28)
 */
public class InsultMessage extends Message {

  private int recipNameSize;
  private byte[] recipUsername;

  /**
   * Constructor for InsultMessage object
   * @param nameSize int representing the size of the sender's username
   * @param username byte array representing the sender's username
   * @param recipNameSize int representing the size of the recipient's username
   * @param recipUsername byte array representing the recipient's username
   * @throws InvalidMessageException
   */
  public InsultMessage(int nameSize, byte[] username, int recipNameSize, byte[] recipUsername) throws InvalidMessageException {
    super(Identifier.SEND_INSULT, nameSize, username);
    this.recipNameSize = recipNameSize;
    this.recipUsername = recipUsername;
  }

  /**
   * Getter method for recipient username size
   * @return int size of recipient's username
   */
  public int getRecipNameSize() {
    return this.recipNameSize;
  }

  /**
   * Getter method for the recipient's username
   * @return byte array of the recipient's username
   */
  public byte[] getRecipUsername() {
    return this.recipUsername;
  }

  /**
   * Getter method for the recipient's username as a String
   * @return String of the recipient's username
   */
  public String getRecipStringName() {
    String s = new String(this.recipUsername, StandardCharsets.UTF_8);
    return s;
  }


}
