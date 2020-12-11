package Communications;

import java.util.HashMap;

public class QueryResponse extends Response {

  //Not really sure what this message is supposed to return...not sure if these fields are correct
  private int numUsers;
  private HashMap<Integer, byte[]> map;

  public QueryResponse(int numUsers, HashMap<Integer, byte[]> map) throws InvalidMessageException {
    super(Identifier.QUERY_USER_RESPONSE, Integer.MIN_VALUE, null);
    this.numUsers = numUsers;
    this.map = map;

  }

  public int getNumUsers() {
    return numUsers;
  }

  public HashMap<Integer, byte[]> getMap() {
    return map;
  }

  public void setNumUsers(int numUsers) {
    this.numUsers = numUsers;
  }

  public void setMap(HashMap<Integer, byte[]> map) {
    this.map = map;
  }
}
