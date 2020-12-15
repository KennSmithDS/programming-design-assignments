import static org.junit.Assert.*;

import Communications.DirectMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DirectMessageTest {

  private DirectMessage dm1;
  private DirectMessage dm2;
  private DirectMessage dm3;
  private String user1 = "isidorac";
  private String user2 = "kendall";
  private String recip1 = "filip_conic";
  private String recip2 = "maia.shen";
  private String message1 = "Hey how are you?";
  private String message2 = "I'm busy!";

  @Before
  public void setUp() throws Exception {

    dm1 = new DirectMessage(user1.length(), user1.getBytes(), recip1.length(), recip1.getBytes(),
        message1.length(), message1.getBytes());
    dm2 = new DirectMessage(user1.length(), user1.getBytes(), recip1.length(), recip1.getBytes(),
        message1.length(), message1.getBytes());
    dm3 = new DirectMessage(user2.length(), user2.getBytes(), recip1.length(), recip1.getBytes(),
        message2.length(), message2.getBytes());

  }

  @Test
  public void getRecipNameSize() {
    Assert.assertEquals(recip1.length(), dm1.getRecipNameSize());
  }

  @Test
  public void getRecipUsername() {
    byte[] recipByte = recip1.getBytes();
    Assert.assertEquals(recipByte.length, dm1.getRecipUsername().length);
    for(int i = 0; i < recipByte.length; i++) {
      Assert.assertEquals(recipByte[i], dm1.getRecipUsername()[i]);
    }
  }

  @Test
  public void getRecipStringName() {
    Assert.assertEquals(recip1, dm1.getRecipStringName());
  }

  @Test
  public void getMsgSize() {
    Assert.assertEquals(message1.length(), dm1.getMsgSize());
  }

  @Test
  public void getMsg() {
    byte[] msgByte = message1.getBytes();
    Assert.assertEquals(msgByte.length, dm1.getMsg().length);
    for(int i = 0; i < msgByte.length; i++) {
      Assert.assertEquals(msgByte[i], dm1.getMsg()[i]);
    }
  }

  @Test
  public void getStringMsg() {
    Assert.assertEquals(message1, dm1.getStringMsg());
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
    String expect = "DirectMessage{recipNameSize=11, recipUsername=[102, 105, 108, 105, 112, 95, 99, "
        + "111, 110, 105, 99], msgSize=16, msg=[72, 101, 121, 32, 104, 111, 119, 32, 97, 114, 101, "
        + "32, 121, 111, 117, 63]} Message{type=null, nameSize=8, username=[105, 115, 105, 100, 111, "
        + "114, 97, 99]}";
    Assert.assertEquals(expect, dm1.toString());
  }

}