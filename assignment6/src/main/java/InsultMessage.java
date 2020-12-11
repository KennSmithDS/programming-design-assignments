public class InsultMessage extends Message {

  private int recipNameSize;
  private byte[] recipUsername;

  public InsultMessage()
      throws InvalidMessageException {
    super(Identifier.SEND_INSULT);
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
