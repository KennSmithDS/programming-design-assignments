public class InsultMessage extends Message {

  private int recipNameSize;
  private byte[] recipUsername;

  public InsultMessage(int nameSize, byte[] username, int recipNameSize, byte[] recipUsername)
      throws InvalidMessageException {
    super(Identifier.SEND_INSULT, nameSize, username);
    this.recipNameSize = recipNameSize;
    this.recipUsername =recipUsername;
  }

  public int getRecipNameSize() {
    return this.recipNameSize;
  }

  public byte[] getRecipUsername() {
    return this.recipUsername;
  }
}
