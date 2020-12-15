package Communications;

import Communications.Communication;
import Communications.Identifier;
import Communications.InvalidMessageException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

/**
 * Abstract (parent) class representing a Message object.
 * Messages are a subtype of the Communication class, where all messages have a sender username
 * (represented as a byte array) and the size of the sender's username.
 */
public abstract class Message extends Communication {

  private Identifier type;
  private int nameSize;
  private byte[] username;

  /**
   * Constructor for a Message object
   * @param type the enum representing the type of Message
   * @param nameSize int representing the size of the sender's username
   * @param username byte array representing the sender's username
   * @throws InvalidMessageException custom exception for invalid message type
   */
  public Message(Identifier type, int nameSize, byte[] username) throws InvalidMessageException {
    super(type);
    this.nameSize = nameSize;
    this.username = username;
  }

  /**
   * Getter method for the sender username size
   * @return size of the sender's username
   */
  public int getNameSize() {
    return this.nameSize;
  }

  /**
   * Getter method for the sender's username (represented as a byte array)
   * @return byte array representing the sender's username
   */
  public byte[] getUsername() {
    return this.username;
  }

  /**
   * Getter method for the String username
   * @return String username
   */
  public String getStringName() {
    String s = new String(this.username, StandardCharsets.UTF_8);
    return s;
  }

  /**
   * Overridden equals method
   * @param o object
   * @return boolean if objects are equal
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Message)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Message message = (Message) o;
    return nameSize == message.nameSize &&
        type == message.type &&
        Arrays.equals(username, message.username);
  }

  /**
   * Overridden hashCode method
   * @return int hashcode
   */
  @Override
  public int hashCode() {
    int result = Objects.hash(super.hashCode(), type, nameSize);
    result = 31 * result + Arrays.hashCode(username);
    return result;
  }

  /**
   * Overridden toString method
   * @return string representing the object
   */
  @Override
  public String toString() {
    return "Message{" +
        "type=" + type +
        ", nameSize=" + nameSize +
        ", username=" + Arrays.toString(username) +
        '}';
  }
}
