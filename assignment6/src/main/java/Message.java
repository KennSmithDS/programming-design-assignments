public abstract class Message extends Communication{

  private Identifier type;
  private int nameSize;
  private byte[] username;

  public Message(Identifier type, int nameSize, byte[] username) throws InvalidMessageException {
    super(type);
    this.nameSize = nameSize;
    this.username = username;
  }

  /*
  public Message(int idValue) throws InvalidMessageException {
    if (Identifier.getIdentifier(idValue) != null) {
      this.message = Identifier.getIdentifier(idValue);
    } else {
      throw new InvalidMessageException("The id value you have provided is not defined.");
    }
  }
   */

  public int getNameSize() {
    return this.nameSize;
  }

  public byte[] getUsername() {
    return this.username;
  }
}
