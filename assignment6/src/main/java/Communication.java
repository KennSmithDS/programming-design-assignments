public abstract class Communication {

  private Identifier type;

  public Communication(Identifier type) throws InvalidMessageException {
    if(type != null) {
      this.type = type;
    } else {
      throw new InvalidMessageException("You have not provided a valid identifier");
    }
  }

  /*
  public Communication(int idValue) throws InvalidMessageException {
    if (Identifier.getIdentifier(idValue) != null) {
      this.message = Identifier.getIdentifier(idValue);
    } else {
      throw new InvalidMessageException("The id value you have provided is not defined.");
    }
  }
   */

  //Get the value of the message identifier
  public int getMessageIdValue() {
    return this.type.getIdentifierValue();
  }

  //Get the "name"/string of the message identifier
  public Identifier getIdentifier() {
    return this.type;
  }


}
