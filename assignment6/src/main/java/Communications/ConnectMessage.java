package Communications;

/**
 * Class that is a subtype of Message to represent CONNECT_MESSAGE (19)
 */
public class ConnectMessage extends Message {

  /**
   * Constructor for ConnectMessage
   * Note that the type is set by default to the CONNECT_MESSAGE enum
   * @param nameSize int representing the size of the sender's username
   * @param username byte array representing the sender's username
   * @throws InvalidMessageException
   */
  public ConnectMessage(int nameSize, byte[] username) throws InvalidMessageException {
    super(Identifier.CONNECT_MESSAGE, nameSize, username);
  }

  //equals(), hashCode(), and toString() don't need to be overriden because there are no additional
  //fields that differ from that of the parent class (Message)

}
