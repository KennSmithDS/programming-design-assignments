package Communications;

/**
 * Non-abstract class that extends Communication for the sake of testing.
 */
public class CommunicationInstance extends Communication {

  /**
   * DefaultCommunication constructor
   * @param type the enum representing the type of communication
   * @throws InvalidMessageException if the type is null
   */
  public CommunicationInstance(Identifier type) throws InvalidMessageException {
    super(type);
  }

}
