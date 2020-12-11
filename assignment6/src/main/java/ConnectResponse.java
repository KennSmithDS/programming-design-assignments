public class ConnectResponse extends Response {

  private boolean success;

  public ConnectResponse() throws InvalidMessageException {
    super(Identifier.CONNECT_RESPONSE);
  }

  public boolean isSuccess() {
    return this.success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

}
