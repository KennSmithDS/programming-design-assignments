package Communications;

import java.util.HashMap;

/**
 * Class that is a subtype of Response to represent QUERY_USER_RESPONSE (24)
 */
public class QueryResponse extends Response {

  private int numUsers;
  private HashMap<Integer, byte[]> users;

  /**
   * Constructor for QueryResponse object
   * @param numUsers int the number of connected users other than the one requesting the query
   * @param users HashMap containing all other connected users (key = username size, value = username
   *              represented by a byte array)
   * @throws InvalidMessageException
   */
  public QueryResponse(int numUsers, HashMap<Integer, byte[]> users) throws InvalidMessageException {
    super(Identifier.QUERY_USER_RESPONSE, Integer.MIN_VALUE, null);
    this.numUsers = numUsers;
    this.users = users;

  }

  /**
   * Getter method for the number of connected users
   * @return int # of connected users
   */
  public int getNumUsers() {
    return numUsers;
  }

  /**
   * Getter method for the HashMap containing connected users' information
   * @return HashMap of connected users' info
   */
  public HashMap<Integer, byte[]> getUsers() {
    return users;
  }

}
