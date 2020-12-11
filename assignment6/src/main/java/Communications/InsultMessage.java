package Communications;

public class InsultMessage extends Message {

  private int recipNameSize;
  private byte[] recipUsername;

  public InsultMessage(int msgSize, byte[] msg, int recipNameSize, byte[] recipUsername) throws InvalidMessageException {
    super(Identifier.SEND_INSULT, msgSize, msg);
    this.recipNameSize = recipNameSize;
    this.recipUsername = recipUsername;
  }

  public int getRecipNameSize() {
    return this.recipNameSize;
  }

  public byte[] getRecipUsername() {
    return this.recipUsername;
  }

  public void setRecipNameSize(int recipNameSize) {
    this.recipNameSize = recipNameSize;
  }

  public void setRecipUsername(byte[] recipUsername) {
    this.recipUsername = recipUsername;
  }

}
