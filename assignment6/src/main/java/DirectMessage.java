public class DirectMessage extends Message {

  private int msgSize;
  private byte[] msg;

  public DirectMessage()
      throws InvalidMessageException {
    super(Identifier.DIRECT_MESSAGE);
  }

  public int getMsgSize() {
    return this.msgSize;
  }

  public byte[] getMsg() {
    return this.msg;
  }

  public void setMsgSize(int msgSize) {
    this.msgSize = msgSize;
  }

  public void setMsg(byte[] msg) {
    this.msg = msg;
  }

}
