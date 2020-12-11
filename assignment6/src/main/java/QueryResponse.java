import java.util.HashMap;

public class QueryResponse extends Response {

  //Not really sure what this message is supposed to return...not sure if these fields are correct
  private int numUsers;
  private HashMap<Integer, Byte[]> map;

  public QueryResponse(int numUsers) throws InvalidMessageException {
    super(Identifier.QUERY_USER_RESPONSE, Integer.MIN_VALUE, null);
  }

}
