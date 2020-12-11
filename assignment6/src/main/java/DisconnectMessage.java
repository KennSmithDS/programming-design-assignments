public class DisconnectMessage extends Message{

  public DisconnectMessage() throws InvalidMessageException {
    super(Identifier.DISCONNECT_MESSAGE);
  }
}
