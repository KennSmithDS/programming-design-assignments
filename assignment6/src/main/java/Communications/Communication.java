package Communications;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Class to represent any kind of communication (i.e. the entire protocol).
 * This is the parent (abstract) class for the two types of communication that occur, message and
 * response (these are also abstract parent classes for their subtypes).
 */
public abstract class Communication implements Serializable {

  private Identifier type;

  /**
   * Communication constructor.
   * @param type the enum representing the type of communication
   * @throws InvalidMessageException if the type is null
   */
  public Communication(Identifier type) throws InvalidMessageException {
    if(type != null) {
      this.type = type;
    } else {
      throw new InvalidMessageException("You have not provided a valid identifier");
    }
  }

  /**
   * Method that gets the int enum value of the identifier.
   * @return
   */
  public int getMessageIdValue() {
    return this.type.getIdentifierValue();
  }

  /**
   * Method that returns the enum representing the type of communication.
   * @return
   */
  public Identifier getIdentifier() {
    return this.type;
  }


  /**
   * Factory method that creates a Communication object based on the data passed in the string param.
   * The method parses the string to get information to pass to the constructor of the
   * appropriate Communication subtype.
   * If the string is not formatted correctly as outlined in the assignment specs, it will catch
   * any error pertaining to processing of this string, and throw an InvalidMs
   * @param data String containing all necessary constructor information
   * @return a Communication object (subtype)
   * @throws InvalidMessageException string is not formatted correctly as outlined in the assignment specs
   */
  public static Communication communicationFactory(String data) throws InvalidMessageException {
    Communication com = null;
    String[] split = data.split("\\s+");
    int type;

    try {
      type = Integer.parseInt(split[0]);
    } catch (Exception e) {
      throw new InvalidMessageException("The input you entered is not valid.");
    }

    switch(type) {

      //CONNECT_MESSAGE
      //ConnectMessage(int nameSize, byte[] username)
      case 19 :
        int nameSize;
        byte[] username;
        try {
          nameSize = Integer.parseInt(split[1]);
          username = split[2].getBytes();
        } catch (Exception e) {
          throw new InvalidMessageException("The input you entered is not valid.");
        }
        com = new ConnectMessage(nameSize, username);
        break;

      //CONNECT_RESPONSE
      //ConnectResponse(int msgSize, byte[] msg, boolean success)
      case 20 :
        int msgSize;
        byte[] msg;
        boolean success;
        try {
          msgSize = Integer.parseInt(split[1]);
          msg = split[2].getBytes();
          success = Boolean.parseBoolean(split[3]);
        } catch (Exception e) {
          throw new InvalidMessageException("The input you entered is not valid.");
        }
        com = new ConnectResponse(msgSize, msg, success);
        break;

      //DISCONNECT_MESSAGE
      //DisconnectMessage(int nameSize, byte[] username)
      case 21 :
        try {
          nameSize = Integer.parseInt(split[1]);
          username = split[2].getBytes();
        } catch (Exception e) {
          throw new InvalidMessageException("The input you entered is not valid.");
        }
        com = new DisconnectMessage(nameSize, username);
        break;

      //DISCONNECT_RESPONSE
      //DisconnectResponse(int msgSize, byte[] msg)
      case 22 :
        try {
          msgSize = Integer.parseInt(split[1]);
          msg = split[2].getBytes();
        } catch (Exception e) {
          throw new InvalidMessageException("The input you entered is not valid.");
        }
        com = new DisconnectResponse(msgSize, msg);
        break;

      //QUERY_USERS
      //QueryUsers(int nameSize, byte[] username)
      case 23 :
        try {
          nameSize = Integer.parseInt(split[1]);
          username = split[2].getBytes();
        } catch (Exception e) {
          throw new InvalidMessageException("The input you entered is not valid.");
        }
        com = new QueryUsers(nameSize, username);
        break;

      //QUERY_RESPONSE
      //QueryResponse(int numUsers, HashMap<Integer, byte[]> map)
      case 24 :
        int numUsers;
        HashMap<Integer, byte[]> map = new HashMap<>();
        try {
          numUsers = Integer.parseInt(split[1]);
          for(int i = 2; i < split.length; i+=2) {
            nameSize = Integer.parseInt(split[i]);
            username = split[i + 1].getBytes();
            map.put(nameSize, username);
          }
        } catch (Exception e) {
          throw new InvalidMessageException("The input you entered is not valid.");
        }
        com = new QueryResponse(numUsers, map);
        break;

      //BROADCAST_MESSAGE
      //BroadcastMessage(int nameSize, byte[] username, int numUsers, int name2Size, byte[] username2)
      case 25 :
        int name2Size;
        byte[] username2;
        try{
          nameSize = Integer.parseInt(split[1]);
          username = split[2].getBytes();
          msgSize = Integer.parseInt(split[3]);
          msg = split[4].getBytes();
        } catch (Exception e) {
          throw new InvalidMessageException("The input you entered is not valid.");
        }
        com = new BroadcastMessage(nameSize, username, msgSize, msg);
        break;

      //DIRECT_MESSAGE
      //DirectMessage(int nameSize, byte[] username, int msgSize, byte[] msg)
      case 26 :
        int recipNameSize;
        byte[] recipUsername;
        try {
          nameSize = Integer.parseInt(split[1]);
          username = split[2].getBytes();
          recipNameSize = Integer.parseInt(split[3]);
          recipUsername = split[4].getBytes();
          msgSize = Integer.parseInt(split[5]);
          msg = split[6].getBytes();
        } catch (Exception e) {
          throw new InvalidMessageException("The input you entered is not valid.");
        }
        com = new DirectMessage(nameSize, username, recipNameSize, recipUsername, msgSize, msg);
        break;

      //FAILED_MESSAGE
      //FailedResponse(int msgSize, byte[] msg)
      case 27 :
        try {
          msgSize = Integer.parseInt(split[1]);
          msg = split[2].getBytes();
        } catch (Exception e) {
          throw new InvalidMessageException("The input you entered is not valid.");
        }
        com = new FailedResponse(msgSize, msg);
        break;

      //SEND_INSULT
      //InsultMessage(int msgSize, byte[] msg, int recipNameSize, byte[] recipUsername)
      case 28 :
        try {
          nameSize = Integer.parseInt(split[1]);
          username = split[2].getBytes();
          recipNameSize = Integer.parseInt(split[3]);
          recipUsername = split[4].getBytes();
        } catch (Exception e) {
          throw new InvalidMessageException("The input you entered is not valid.");
        }
        com = new InsultMessage(nameSize, username, recipNameSize, recipUsername);
        break;

      default :
        throw new InvalidMessageException("The number you have entered is not associated with a message or response.");
    }

    return com;

  }





}
