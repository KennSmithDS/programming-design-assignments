import java.util.HashMap;

public class QueryResponse extends Response {

  //Not really sure what this message is supposed to return...not sure if these fields are correct
  private int numUsers;
  private HashMap<Integer, Byte[]> map;

  public QueryResponse() throws InvalidMessageException {
    super(Identifier.QUERY_USER_RESPONSE);
    setMsgSize(Integer.MIN_VALUE);
    setMsg(null);
  }

  public int getNumUsers() {
    return numUsers;
  }

  public HashMap<Integer, Byte[]> getMap() {
    return map;
  }

  public void setNumUsers(int numUsers) {
    this.numUsers = numUsers;
  }

  public void setMap(HashMap<Integer, Byte[]> map) {
    this.map = map;
  }
}
