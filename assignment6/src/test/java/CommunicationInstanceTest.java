import static org.junit.Assert.*;

import Communications.BroadcastMessage;
import Communications.Communication;
import Communications.CommunicationInstance;
import Communications.ConnectMessage;
import Communications.ConnectResponse;
import Communications.DirectMessage;
import Communications.DisconnectMessage;
import Communications.DisconnectResponse;
import Communications.FailedResponse;
import Communications.Identifier;
import Communications.InsultMessage;
import Communications.InvalidMessageException;
import Communications.QueryResponse;
import Communications.QueryUsers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CommunicationInstanceTest {

  private CommunicationInstance c1;
  private CommunicationInstance c2;
  private CommunicationInstance c3;
  private Communication result;

  @Before
  public void setUp() throws Exception {

    c1 = new CommunicationInstance(Identifier.CONNECT_MESSAGE);
    c2 = new CommunicationInstance(Identifier.CONNECT_MESSAGE);
    c3 = new CommunicationInstance(Identifier.CONNECT_RESPONSE);

  }

  @Test (expected = InvalidMessageException.class)
  public void comFactoryException() throws InvalidMessageException {
    String test = "invalid string format";
    result = Communication.communicationFactory(test);
  }

  @Test
  public void comFactory19() throws InvalidMessageException {
    String input = "19 7 isidora";
    result = Communication.communicationFactory(input);
    Assert.assertTrue(result instanceof ConnectMessage);
    Assert.assertEquals("isidora", ((ConnectMessage) result).getStringName());
    Assert.assertEquals(7, ((ConnectMessage) result).getNameSize());
  }

  @Test
  public void comFactory20() throws InvalidMessageException {
    String input = "20 13 now connected true";
    result = Communication.communicationFactory(input);
    Assert.assertTrue(result instanceof ConnectResponse);
    Assert.assertEquals("now connected ", ((ConnectResponse) result).getStringMsg());
    Assert.assertEquals(13, ((ConnectResponse) result).getMsgSize());
    Assert.assertEquals(true, ((ConnectResponse) result).isSuccess());
  }

  @Test
  public void comFactory21() throws InvalidMessageException {
    String input = "21 7 isidora";
    result = Communication.communicationFactory(input);
    Assert.assertTrue(result instanceof DisconnectMessage);
    Assert.assertEquals("isidora", ((DisconnectMessage) result).getStringName());
    Assert.assertEquals(7, ((DisconnectMessage) result).getNameSize());
  }

  @Test
  public void comFactory22() throws InvalidMessageException {
    String input = "22 16 now disconnected";
    result = Communication.communicationFactory(input);
    Assert.assertTrue(result instanceof DisconnectResponse);
    Assert.assertEquals("now disconnected ", ((DisconnectResponse) result).getStringMsg());
    Assert.assertEquals(16, ((DisconnectResponse) result).getMsgSize());
  }

  @Test
  public void comFactory23() throws InvalidMessageException {
    String input = "23 7 isidora";
    result = Communication.communicationFactory(input);
    Assert.assertTrue(result instanceof QueryUsers);
    Assert.assertEquals("isidora", ((QueryUsers) result).getStringName());
    Assert.assertEquals(7, ((QueryUsers) result).getNameSize());
  }

  @Test
  public void comFactory24() throws InvalidMessageException {
    String input = "24 2 7 isidora 5 filip";
    result = Communication.communicationFactory(input);
    Assert.assertTrue(result instanceof QueryResponse);
    Assert.assertEquals(2, ((QueryResponse) result).getNumUsers());
    Assert.assertEquals(2, ((QueryResponse) result).getUsers().size());
  }

  @Test
  public void comFactory25() throws InvalidMessageException {
    String input = "25 7 isidora 6 hello!";
    result = Communication.communicationFactory(input);
    Assert.assertTrue(result instanceof BroadcastMessage);
    Assert.assertEquals("isidora", ((BroadcastMessage) result).getStringName());
    Assert.assertEquals(7, ((BroadcastMessage) result).getNameSize());
    Assert.assertEquals("hello! ", ((BroadcastMessage) result).getStringMsg());
    Assert.assertEquals(6, ((BroadcastMessage) result).getMsgSize());
  }

  @Test
  public void comFactory26() throws InvalidMessageException {
    String input = "26 7 isidora 5 filip 7 how ru?";
    result = Communication.communicationFactory(input);
    Assert.assertTrue(result instanceof DirectMessage);
    Assert.assertEquals("isidora", ((DirectMessage) result).getStringName());
    Assert.assertEquals(7, ((DirectMessage) result).getNameSize());
    Assert.assertEquals("filip", ((DirectMessage) result).getRecipStringName());
    Assert.assertEquals(5, ((DirectMessage) result).getRecipNameSize());
    Assert.assertEquals("how ru? ", ((DirectMessage) result).getStringMsg());
    Assert.assertEquals(7, ((DirectMessage) result).getMsgSize());
  }

  @Test
  public void comFactory27() throws InvalidMessageException {
    String input = "27 7 failure";
    result = Communication.communicationFactory(input);
    Assert.assertTrue(result instanceof FailedResponse);
    Assert.assertEquals("failure ", ((FailedResponse) result).getStringMsg());
    Assert.assertEquals(7, ((FailedResponse) result).getMsgSize());
  }

  @Test
  public void comFactory28() throws InvalidMessageException {
    String input = "28 7 isidora 5 filip";
    result = Communication.communicationFactory(input);
    Assert.assertTrue(result instanceof InsultMessage);
    Assert.assertEquals("isidora", ((InsultMessage) result).getStringName());
    Assert.assertEquals(7, ((InsultMessage) result).getNameSize());
    Assert.assertEquals("filip", ((InsultMessage) result).getRecipStringName());
    Assert.assertEquals(5, ((InsultMessage) result).getRecipNameSize());
  }

  @Test
  public void equalsTrue() {
    Assert.assertTrue(c1.equals(c2));
  }

  @Test
  public void equalsFalse() {
    Assert.assertFalse(c1.equals(c3));
  }

  @Test
  public void equalsFalseObject() {
    Assert.assertFalse(c1.equals("hello!"));
  }

  @Test
  public void hashCodeTest() {
    Assert.assertEquals(c1.hashCode(), c2.hashCode());
  }

  @Test
  public void toStringTest() {
    String expected = "Communication{type=CONNECT_MESSAGE}";
    Assert.assertEquals(c1.toString(), expected);
  }

}