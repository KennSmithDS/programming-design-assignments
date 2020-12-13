package Communications;

/**
 * Class that is a subtype of Response to represent CONNECT_RESPONSE (20)
 */
public class ConnectResponse extends Response {

  private boolean success;

  /**
   * Constructor for ConnectResponse
   * @param msgSize size of the message sent in response to a CONNECT_MESSAGE
   * @param msg message represented as a byte array containing the response
   * @param success true if connection was successful, false otherwise
   * @throws InvalidMessageException
   */
  public ConnectResponse(int msgSize, byte[] msg, boolean success) throws InvalidMessageException {
    super(Identifier.CONNECT_RESPONSE, msgSize, msg);
    this.success = success;
  }

  /**
   * Getter method for the success field
   * @return boolean represnting if the connection was successful
   */
  public boolean isSuccess() {
    return this.success;
  }

}
