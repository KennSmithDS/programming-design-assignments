package Communications;

import Communications.Communication;
import Communications.Identifier;
import Communications.InvalidMessageException;
import java.nio.charset.StandardCharsets;

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
   * @throws InvalidMessageException
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

  public String getStringName() {
    String s = new String(this.username, StandardCharsets.UTF_8);
    return s;
  }

}
