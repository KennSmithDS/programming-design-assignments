public class DisconnectResponse extends Response {

  public DisconnectResponse(int msgSize, byte[] msg)
      throws InvalidMessageException {
    super(Identifier.DISCONNECT_MESSAGE, msgSize, msg);
  }

}
