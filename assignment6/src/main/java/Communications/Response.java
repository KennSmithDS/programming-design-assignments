package Communications;

import Communications.Communication;
import Communications.Identifier;
import Communications.InvalidMessageException;

public abstract class Response extends Communication {

  private int msgSize;
  private byte[] msg;

  public Response(Identifier type, int msgSize, byte[] msg) throws InvalidMessageException {
    super(type);
    this.msgSize = msgSize;
    this.msg = msg;
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
