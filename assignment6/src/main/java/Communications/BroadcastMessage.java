package Communications;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

/**
 * Class representing a BROADCAST_MESSAGE object.
 * It is a subtype of the Message class.
 */
public class BroadcastMessage extends Message {

  private int msgSize;
  private byte[] msg;

  /**
   * Constructor for BroadcastMessage
   * @param nameSize int representing the size of the sender's username
   * @param username byte array representing the sender's username
   * @param msgSize int representing the size of the message
   * @param msg byte array representing the message
   * @throws InvalidMessageException custom exception for invalid message type
   */
  public BroadcastMessage(int nameSize, byte[] username, int msgSize, byte[] msg) throws InvalidMessageException {
    super(Identifier.BROADCAST_MESSAGE, nameSize, username);
    this.msgSize = msgSize;
    this.msg = msg;
  }

  /**
   * Getter method for the message size
   * @return size of message to be sent
   */
  public int getMsgSize() {
    return msgSize;
  }

  /**
   * Getter method for the message
   * @return message to be sent as a byte array
   */
  public byte[] getMsg() {
    return msg;
  }

  /**
   * Getter method that returns the message as a String
   * @return msg as a String
   */
  public String getStringMsg() {
    String s = new String(this.msg, StandardCharsets.UTF_8);
    return s;
  }

  /**
   * Overriden equals method to include params that aren't in the parent Message class
   * @param o object
   * @return boolean are equal
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BroadcastMessage)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    BroadcastMessage that = (BroadcastMessage) o;
    return msgSize == that.msgSize &&
        Arrays.equals(msg, that.msg);
  }

  /**
   * Overridden hashCode method to include params that aren't in the parent Message class
   * @return int hashCode
   */
  @Override
  public int hashCode() {
    int result = Objects.hash(super.hashCode(), msgSize);
    result = 31 * result + Arrays.hashCode(msg);
    return result;
  }

  /**
   * Overridden toString method to include params that aren't in the parent Message class
   * @return String
   */
  @Override
  public String toString() {
    return "BroadcastMessage{" +
        "msgSize=" + msgSize +
        ", msg=" + Arrays.toString(msg) +
        "} " + super.toString();
  }
}
