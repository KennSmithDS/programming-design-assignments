public class DisconnectResponse extends Response {

  public DisconnectResponse()
      throws InvalidMessageException {
    super(Identifier.DISCONNECT_MESSAGE);
  }

}
