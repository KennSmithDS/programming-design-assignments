package Communications;

/**
 * Class that is a subtype of Response to represent DISCONNECT_RESPONSE (22)
 */
public class DisconnectResponse extends Response {

  /**
   * Constructor for DisconnectResponse object.
   * @param msgSize size of the message sent in response to a DISCONNECT_MESSAGE
   * @param msg message represented as a byte array containing the response
   * @throws InvalidMessageException
   */
  public DisconnectResponse(int msgSize, byte[] msg) throws InvalidMessageException {
    super(Identifier.DISCONNECT_MESSAGE, msgSize, msg);
  }

}
