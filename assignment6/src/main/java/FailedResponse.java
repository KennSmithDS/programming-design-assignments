public class FailedResponse extends Response {

  public FailedResponse(int msgSize, byte[] msg) throws InvalidMessageException {
    super(Identifier.FAILED_MESSAGE, msgSize, msg);
  }

}
