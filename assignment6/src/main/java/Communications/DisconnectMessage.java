package Communications;

/**
 * Class that is a subtype of Message to represent DISCONNECT_MESSAGE (21)
 */
public class DisconnectMessage extends Message {

  /**
   * Constructor for DisconnectMessage
   * Note that the type is set by default to the DISCONNECT_MESSAGE enum
   * @param nameSize int representing the size of the sender's username
   * @param username byte array representing the sender's username
   * @throws InvalidMessageException
   */
  public DisconnectMessage(int nameSize, byte[] username) throws InvalidMessageException {
    super(Identifier.DISCONNECT_MESSAGE, nameSize, username);
  }
}
