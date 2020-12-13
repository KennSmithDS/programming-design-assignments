package Communications;

/**
 * Class that is a subtype of Response to represent FAILED_MESSAGE (27)
 */
public class FailedResponse extends Response {

  /**
   *
   * @param msgSize size of the message sent in response to a failed message sent by a user
   * @param msg message represented as a byte array describing the failure
   * @throws InvalidMessageException
   */
  public FailedResponse(int msgSize, byte[] msg) throws InvalidMessageException {
    super(Identifier.FAILED_MESSAGE, msgSize, msg);
  }

}
