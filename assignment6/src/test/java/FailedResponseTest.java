import static org.junit.Assert.*;

import Communications.FailedResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;

public class FailedResponseTest {

  private FailedResponse fr1;
  private FailedResponse fr2;
  private FailedResponse fr3;
  String message1 = "The user doesn't exist.";
  String message2 = "Username already in use";


  @Before
  public void setUp() throws Exception {

    fr1 = new FailedResponse(message1.length(), message1.getBytes());
    fr2 = new FailedResponse(message1.length(), message1.getBytes());
    fr3 = new FailedResponse(message2.length(), message2.getBytes());

  }

  @Test
  public void getMsgSize() {
    Assert.assertEquals(message1.length(), fr1.getMsgSize());
  }

  @Test
  public void getMsg() {
    byte[] msgByte = message1.getBytes();
    Assert.assertEquals(msgByte.length, fr1.getMsg().length);
    for(int i = 0; i < msgByte.length; i++) {
      Assert.assertEquals(msgByte[i], fr1.getMsg()[i]);
    }
  }

  @Test
  public void getStringMsg() {
    Assert.assertEquals(message1, fr1.getStringMsg());
  }

  @Test
  public void equalsTrue() {
    Assert.assertTrue(fr1.equals(fr2));
  }

  @Test
  public void equalsFalse() {
    Assert.assertFalse(fr1.equals(fr3));
  }

  @Test
  public void equalsFalseObject() {
    Assert.assertFalse(fr1.equals("hello"));
  }

  @Test
  public void hashCodeTest() {
    Assert.assertEquals(fr1.hashCode(), fr2.hashCode());
  }

  @Test
  public void toStringTest() {
    String expect = "Response{msgSize=23, msg=[84, 104, 101, 32, 117, 115, 101, 114, 32, 100, 111, "
        + "101, 115, 110, 39, 116, 32, 101, 120, 105, 115, 116, 46]} Communication{type=FAILED_MESSAGE}";
    Assert.assertEquals(expect, fr1.toString());
  }

}