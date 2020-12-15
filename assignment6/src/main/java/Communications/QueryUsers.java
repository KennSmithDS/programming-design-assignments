package Communications;

import Communications.Identifier;
import Communications.InvalidMessageException;
import Communications.Message;

/**
 * Class that is a subtype of Message to represent QUERY_CONNECTED_USERS (23)
 */
public class QueryUsers extends Message {

  /**
   * Constructor for QueryUsers object
   * @param nameSize int representing the size of the sender's username
   * @param username byte array representing the sender's username
   * @throws InvalidMessageException custom exception for invalid message type
   */
  public QueryUsers(int nameSize, byte[] username) throws InvalidMessageException {
    super(Identifier.QUERY_CONNECTED_USERS, nameSize, username);
  }

}
