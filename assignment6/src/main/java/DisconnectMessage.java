public class DisconnectMessage extends Message{

  public DisconnectMessage(int nameSize, byte[] username) throws InvalidMessageException {
    super(Identifier.DISCONNECT_MESSAGE, nameSize, username);
  }
}
