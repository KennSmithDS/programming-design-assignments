public class QueryUsers extends Message {

  public QueryUsers()
      throws InvalidMessageException {
    super(Identifier.QUERY_CONNECTED_USERS);
  }



}
