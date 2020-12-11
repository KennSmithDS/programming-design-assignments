package Communications;

public class ConnectMessage extends Message {

  public ConnectMessage(int nameSize, byte[] username) throws InvalidMessageException {
    super(Identifier.CONNECT_MESSAGE, nameSize, username);
  }

}
