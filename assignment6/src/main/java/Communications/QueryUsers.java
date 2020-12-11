package Communications;

import Communications.Identifier;
import Communications.InvalidMessageException;
import Communications.Message;

public class QueryUsers extends Message {

  public QueryUsers(int nameSize, byte[] username) throws InvalidMessageException {
    super(Identifier.QUERY_CONNECTED_USERS, nameSize, username);
  }



}
