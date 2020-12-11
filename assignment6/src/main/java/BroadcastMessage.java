public class BroadcastMessage extends Message{

  private int numUsers;
  private int name2Size;
  private byte[] username2;

  public BroadcastMessage(int numUsers, int name1Size, byte[] username1, int name2Size, byte[] username2)
      throws InvalidMessageException {
    super(Identifier.BROADCAST_MESSAGE, name1Size, username1);
    this.numUsers = numUsers;
    this.name2Size = name2Size;
    this.username2 = username2;
  }

  public int getNumUsers() {
    return this.numUsers;
  }

  public int getName2Size() {
    return this.name2Size;
  }

  public byte[] getUsername2() {
    return this.username2;
  }
}
