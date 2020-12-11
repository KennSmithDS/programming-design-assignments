package Communications;

import Communications.Communication;
import Communications.Identifier;
import Communications.InvalidMessageException;

public abstract class Message extends Communication {

  private Identifier type;
  private int nameSize;
  private byte[] username;

  public Message(Identifier type, int nameSize, byte[] username) throws InvalidMessageException {
    super(type);
    this.nameSize = nameSize;
    this.username = username;
  }

  public int getNameSize() {
    return this.nameSize;
  }

  public byte[] getUsername() {
    return this.username;
  }

  public void setNameSize(int nameSize) {
    this.nameSize = nameSize;
  }

  public void setUsername(byte[] username) {
    this.username = username;
  }
}
