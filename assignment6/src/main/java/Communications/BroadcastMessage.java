package Communications;

public class BroadcastMessage extends Message {

  private int numUsers;
  private int name2Size;
  private byte[] username2;

  public BroadcastMessage(int nameSize, byte[] username, int numUsers, int name2Size, byte[] username2) throws InvalidMessageException {
    super(Identifier.BROADCAST_MESSAGE, nameSize, username);
    this.numUsers = numUsers;
    this.name2Size =name2Size;
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

  public void setNumUsers(int numUsers) {
    this.numUsers = numUsers;
  }

  public void setName2Size(int name2Size) {
    this.name2Size = name2Size;
  }

  public void setUsername2(byte[] username2) {
    this.username2 = username2;
  }
}
