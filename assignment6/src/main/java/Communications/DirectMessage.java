package Communications;

import java.nio.charset.StandardCharsets;

/**
 * Class representing a DISCONNECT_MESSAGE object.
 * It is a subtype of the Message class.
 */
public class DirectMessage extends Message {

  private int recipNameSize;
  private byte[] recipUsername;
  private int msgSize;
  private byte[] msg;

  /**
   * Constructor for DirectMessage object.
   * @param nameSize int representing the size of the sender's username
   * @param username byte array representing the sender's username
   * @param recipNameSize int representing the size of the recipient's username
   * @param recipUsername byte array representing the recipient's username
   * @param msgSize size of the message to be sent to recipient
   * @param msg message represented as a byte array
   * @throws InvalidMessageException custom exception for invalid message type
   */
  public DirectMessage(int nameSize, byte[] username, int recipNameSize, byte[] recipUsername,
      int msgSize, byte[] msg) throws InvalidMessageException {
    super(Identifier.DIRECT_MESSAGE, nameSize, username);
    this.recipNameSize = recipNameSize;
    this.recipUsername = recipUsername;
    this.msgSize = msgSize;
    this.msg = msg;
  }

  /**
   * Getter method for recipient username size
   * @return int size of recipient's username
   */
  public int getRecipNameSize() {
    return recipNameSize;
  }

  /**
   * Getter method for the recipient's username
   * @return byte array of the recipient's username
   */
  public byte[] getRecipUsername() {
    return recipUsername;
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
   * Getter method for the size of the direct message
   * @return int size of the message
   */
  public int getMsgSize() {
    return msgSize;
  }

  /**
   * Getter method for the direct message
   * @return byte array of the message to be sent to the recipient
   */
  public byte[] getMsg() {
    return msg;
  }

  /**
   * Method to get the UTF8 encoded string of byte array message
   * @return String UTF8 message
   */
  public String getStringMsg() {
    String s = new String(this.msg, StandardCharsets.UTF_8);
    return s;
  }
}
