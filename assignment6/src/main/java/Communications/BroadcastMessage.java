package Communications;

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
   * @throws InvalidMessageException
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
}
