package Communications;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

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
   * @throws InvalidMessageException custom exception for invalid message type
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

  /**
   * Overridden equals method for ConnectResponse objects
   * @param o object
   * @return boolean if the objects are equal
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ConnectResponse)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    ConnectResponse that = (ConnectResponse) o;
    return success == that.success;
  }

  /**
   * Overridden hashcode
   * @return int hashcode of ConnectResponse object
   */
  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), success);
  }

  /**
   * Overridden toString method for ConnectResponse
   * @return string representation of the object
   */
  @Override
  public String toString() {
    return "ConnectResponse{" +
        "success=" + success +
        "} " + super.toString();
  }
}
