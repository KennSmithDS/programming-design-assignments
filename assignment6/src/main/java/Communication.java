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


  public static Communication communicationFactory(int type) throws InvalidMessageException {
    Communication com = null;
    switch(type) {

      case 19 :
        com = new ConnectMessage();
        break;

      case 20 :
        com = new ConnectResponse();
        break;

      case 21 :
        com = new DisconnectMessage();
        break;

      case 22 :
        com = new QueryUsers();
        break;

      case 23 :
        com = new QueryResponse();
        break;

      case 24 :
        com = new BroadcastMessage();
        break;

      case 25 :
        com = new DirectMessage();
        break;

      case 26 :
        com = new FailedResponse();
        break;

      case 27 :
        com = new InsultMessage();
        break;

      default :
        throw new InvalidMessageException("The number you have entered is not associated with a message or response.");
    }

    return com;

  }



}
