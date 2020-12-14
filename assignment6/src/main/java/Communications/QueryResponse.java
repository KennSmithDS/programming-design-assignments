package Communications;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * Class that is a subtype of Response to represent QUERY_USER_RESPONSE (24)
 */
public class QueryResponse extends Response {

  private int numUsers;
  private HashMap<byte[], Integer> users;

  /**
   * Constructor for QueryResponse object
   * @param numUsers int the number of connected users other than the one requesting the query
   * @param users HashMap containing all other connected users (key = username size, value = username
   *              represented by a byte array)
   * @throws InvalidMessageException
   */
  public QueryResponse(int numUsers, HashMap<byte[], Integer> users) throws InvalidMessageException {
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
  public HashMap<byte[], Integer> getUsers() {
    return users;
  }

  /**
   * Method that prints all the users that are currently connected (in the HashMap)
   */
  public void printUsers() {
    for(byte[] username : users.keySet()) {
      String s = new String(username, StandardCharsets.UTF_8);
      System.out.println(s);
    }
  }

}
