public class FailedResponse extends Response {

  public FailedResponse() throws InvalidMessageException {
    super(Identifier.FAILED_MESSAGE);
  }

}
