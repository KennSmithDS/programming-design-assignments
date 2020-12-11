package Communications;

import Communications.Identifier;
import Communications.InvalidMessageException;
import Communications.Message;

public class QueryUsers extends Message {

  public QueryUsers(int msgSize, byte[] msg) throws InvalidMessageException {
    super(Identifier.QUERY_CONNECTED_USERS, msgSize, msg);
  }



}
