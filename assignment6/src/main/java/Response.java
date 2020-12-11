public abstract class Response extends Communication {

  private int msgSize;
  private byte[] msg;

  public Response(Identifier type) throws InvalidMessageException {
    super(type);
  }

  public void setMsgSize(int msgSize) {
    this.msgSize = msgSize;
  }

  public void setMsg(byte[] msg) {
    this.msg = msg;
  }

  public int getMsgSize() {
    return this.msgSize;
  }

  public byte[] getMsg() {
    return this.msg;
  }
}
