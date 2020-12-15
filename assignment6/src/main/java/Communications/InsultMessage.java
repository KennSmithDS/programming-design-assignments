package Communications;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

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
   * @throws InvalidMessageException custom exception for invalid message type
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

  /**
   * Overridden equals method for InsultMessage objects
   * @param o
   * @return boolean if the objects are equal
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof InsultMessage)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    InsultMessage that = (InsultMessage) o;
    return recipNameSize == that.recipNameSize &&
        Arrays.equals(recipUsername, that.recipUsername);
  }

  /**
   * Overridden hashcode method for InsultMessage objects
   * @return int hashcode
   */
  @Override
  public int hashCode() {
    int result = Objects.hash(super.hashCode(), recipNameSize);
    result = 31 * result + Arrays.hashCode(recipUsername);
    return result;
  }

  /**
   * Overridden toString method for InsultMessage objects
   * @return string representation of the InsultMessage object
   */
  @Override
  public String toString() {
    return "InsultMessage{" +
        "recipNameSize=" + recipNameSize +
        ", recipUsername=" + Arrays.toString(recipUsername) +
        "} " + super.toString();
  }
}
