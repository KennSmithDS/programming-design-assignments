import static org.junit.Assert.*;

import Communications.BroadcastMessage;
import Communications.InvalidMessageException;
import Communications.Message;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class BroadcastMessageTest {

  private BroadcastMessage bm1;
  private BroadcastMessage bm2;
  private String message = "Hello everyone!";
  private byte[] byteMsg = message.getBytes();


  @Before
  public void setUp() throws Exception {

    String username1 = "isidorac";
    int username1length = username1.length();
    byte[] username1byte = username1.getBytes();
    int msgSize = message.length();
    bm1 = new BroadcastMessage(username1length, username1byte, msgSize, byteMsg);
    bm2 = null;
  }

  @Test
  public void getMsgSize() {
    Assert.assertEquals(15, bm1.getMsgSize());
  }

  @Test
  public void getMsg() {
    byte[] msg = this.message.getBytes();
    Assert.assertEquals(msg.length, bm1.getMsg().length);
    for(int i = 0; i < msg.length; i++) {
      Assert.assertEquals(msg[i], bm1.getMsg()[i]);
    }
  }

  @Test
  public void getStringMsg() {
    Assert.assertEquals(this.message, bm1.getStringMsg());
  }

  @Test
  public void getNameSize() {
    Assert.assertEquals(8, bm1.getNameSize());
  }

  @Test
  public void getUsername() {
    String name = "isidorac";
    byte[] nameByte = name.getBytes();
    Assert.assertEquals(nameByte.length, bm1.getUsername().length);
    for(int i = 0; i < nameByte.length; i++) {
      Assert.assertEquals(nameByte[i], bm1.getUsername()[i]);
    }
  }

  @Test
  public void getStringName() {
    Assert.assertEquals("isidorac", bm1.getStringName());
  }

  @Test
  public void equalsSame() throws InvalidMessageException {
    String name = "isidorac";
    byte[] nameByte = name.getBytes();
    bm2 = new BroadcastMessage(name.length(), nameByte, message.length(), message.getBytes());
    Assert.assertTrue(bm1.equals(bm2));
  }

  @Test
  public void equalsNull() throws InvalidMessageException {
    Assert.assertFalse(bm1.equals(bm2));
  }

  @Test
  public void equalsDiffType() {
    String s = "hello!";
    Assert.assertFalse(bm1.equals(s));
  }

  @Test
  public void hashCodeTest() throws InvalidMessageException {
    String name = "isidorac";
    byte[] nameByte = name.getBytes();
    bm2 = new BroadcastMessage(name.length(), nameByte, message.length(), message.getBytes());
    Assert.assertEquals(bm1.hashCode(), bm2.hashCode());
  }

  @Test
  public void toStringTest() {
    String expect = "BroadcastMessage{msgSize=15, msg=[72, 101, 108, 108, 111, 32, 101, 118, 101, 114, 121, 111, 110, 101, 33]} Message{type=null, nameSize=8, username=[105, 115, 105, 100, 111, 114, 97, 99]}";
    Assert.assertEquals(expect, bm1.toString());
  }



}