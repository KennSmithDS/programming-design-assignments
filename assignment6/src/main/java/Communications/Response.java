package Communications;

import Communications.Communication;
import Communications.Identifier;
import Communications.InvalidMessageException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

/**
 * Abstract (parent) class representing a Response object.
 * Responses are a subtype of the Communication class, where all responses have a message
 * (represented as a byte array) and the size of the that message (int).
 */
public abstract class Response extends Communication {

  private int msgSize;
  private byte[] msg;

  /**
   * Constructor for a Response object.
   * @param type the enum representing the type of Response
   * @param msgSize size of the message sent in response to a CONNECT_MESSAGE
   * @param msg message represented as a byte array containing the response
   * @throws InvalidMessageException custom exception for invalid message type
   */
  public Response(Identifier type, int msgSize, byte[] msg) throws InvalidMessageException {
    super(type);
    this.msgSize = msgSize;
    this.msg = msg;
  }

  /**
   * Getter method for the size of the message to be sent in response
   * @return int size of message to be sent in response
   */
  public int getMsgSize() {
    return this.msgSize;
  }

  /**
   * Getter method for the message sent in response.
   * @return byte array representing the message sent in response.
   */
  public byte[] getMsg() {
    return this.msg;
  }

  /**
   * Getter method for String message
   * @return meessage as a String
   */
  public String getStringMsg() {
    String s = new String(this.msg, StandardCharsets.UTF_8);
    return s;
  }

  /**
   * Overridden equals method for Response objects
   * @param o object
   * @return boolean if the objects are equal
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Response)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Response response = (Response) o;
    return msgSize == response.msgSize &&
        Arrays.equals(msg, response.msg);
  }

  /**
   * Overridden hashcode
   * @return int hashcode of Response object
   */
  @Override
  public int hashCode() {
    int result = Objects.hash(super.hashCode(), msgSize);
    result = 31 * result + Arrays.hashCode(msg);
    return result;
  }

  /**
   * Overridden toString method for Response
   * @return string representation of the object
   */
  @Override
  public String toString() {
    return "Response{" +
        "msgSize=" + msgSize +
        ", msg=" + Arrays.toString(msg) +
        "} " + super.toString();
  }
}
