public abstract class Message {

  private Identifier message;

  public Message(Identifier message) throws InvalidMessageException {
    if(message != null) {
      this.message = message;
    } else {
      throw new InvalidMessageException("You have not provided a valid identifier");
    }
  }

  public Message(int idValue) throws InvalidMessageException {
    if (Identifier.getIdentifier(idValue) != null) {
      this.message = Identifier.getIdentifier(idValue);
    } else {
      throw new InvalidMessageException("The id value you have provided is not defined.");
    }
  }

  //Get the value of the message identifier
  public int getMessageIdValue() {
    return this.message.getIdentifierValue();
  }

  //Get the "name"/string of the message identifier
  public Identifier getIdentifier() {
    return this.message;
  }


}
