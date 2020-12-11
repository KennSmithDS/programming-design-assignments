package Communications;

public class ConnectResponse extends Response {

  private boolean success;

  public ConnectResponse(int msgSize, byte[] msg, boolean success) throws InvalidMessageException {
    super(Identifier.CONNECT_RESPONSE, msgSize, msg);
    this.success = success;
  }

  public boolean isSuccess() {
    return this.success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

}
