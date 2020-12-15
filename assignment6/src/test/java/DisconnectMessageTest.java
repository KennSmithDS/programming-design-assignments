import static org.junit.Assert.*;

import Communications.DisconnectMessage;
import Communications.DisconnectResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DisconnectMessageTest {

  private DisconnectMessage dm1;
  private DisconnectMessage dm2;
  private DisconnectMessage dm3;
  private String user1 = "isidora";
  private String user2 = "kendall";

  @Before
  public void setUp() throws Exception {

    dm1 = new DisconnectMessage(user1.length(), user1.getBytes());
    dm2 = new DisconnectMessage(user1.length(), user1.getBytes());
    dm3 = new DisconnectMessage(user2.length(), user2.getBytes());

  }

  @Test
  public void getNameSize() {
    Assert.assertEquals(user1.length(), dm1.getNameSize());
  }

  @Test
  public void getUsername() {
    byte[] nameByte = user1.getBytes();
    Assert.assertEquals(nameByte.length, dm1.getUsername().length);
    for(int i = 0; i < nameByte.length; i++) {
      Assert.assertEquals(nameByte[i], dm1.getUsername()[i]);
    }
  }

  @Test
  public void getStringName() {
    Assert.assertEquals(user1, dm1.getStringName());
  }

  @Test
  public void equalsTrue() {
    Assert.assertTrue(dm1.equals(dm2));
  }

  @Test
  public void equalsFalse() {
    Assert.assertFalse(dm1.equals(dm3));
  }

  @Test
  public void equalsFalseObject() {
    Assert.assertFalse(dm1.equals("hello"));
  }

  @Test
  public void hashCodeTest() {
    Assert.assertEquals(dm1.hashCode(), dm2.hashCode());
  }

  @Test
  public void toStringTest() {
    String expect = "Message{type=null, nameSize=7, username=[105, 115, 105, 100, 111, 114, 97]}";
    Assert.assertEquals(expect, dm1.toString());
  }


}