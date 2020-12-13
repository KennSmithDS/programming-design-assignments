package Communications;

import java.util.HashMap;

/**
 * Enum class to represent all the different types of messages (protocol) that our chat app supports
 */
public enum Identifier {

  CONNECT_MESSAGE(19),
  CONNECT_RESPONSE(20),
  DISCONNECT_MESSAGE(21),
  DISCONNECT_RESPONSE(22),
  QUERY_CONNECTED_USERS(23),
  QUERY_USER_RESPONSE(24),
  BROADCAST_MESSAGE(25),
  DIRECT_MESSAGE(26),
  FAILED_MESSAGE(27),
  SEND_INSULT(28);

  private final int identifierValue;
  private static HashMap<Integer, Identifier> map = new HashMap<>();

  //Set up a HashMap so that we can access the Communications.Identifier given just the message identifier value
  static {
    for(Identifier id : Identifier.values()) {
      map.put(id.identifierValue, id);
    }
  }

  /**
   * Constructor for the enum class
   * @param identifierValue takes int associated to that message/communication type
   */
  Identifier(int identifierValue) {
    this.identifierValue = identifierValue;
  }

  /**
   * Getter method for the int value associated with each type of message/communication
   * @return int associated with that Identifier
   */
  public int getIdentifierValue() {
    return this.identifierValue;
  }

  /**
   * Getter method for the actual Identifier itself
   * @param value int for which we want the identifier
   * @return identifier
   */
  public static Identifier getIdentifier(int value) {
    if(map.containsKey(value)) {
      return map.get(value);
    } else {
      return null;
    }
  }

}
