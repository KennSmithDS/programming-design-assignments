import Communications.ConnectResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConnectResponseTest {

  private ConnectResponse cr1;
  private ConnectResponse cr2;
  private ConnectResponse cr3;
  private String message = "The connection was successful";


  @Before
  public void setUp() throws Exception {
    byte[] msg = message.getBytes();
    int msgLength = message.length();
    String diffMsg = "The connection was unsuccessful";
    cr1 = new ConnectResponse(msgLength, msg, true);
    cr2 = new ConnectResponse(msgLength, msg, true);
    cr3 = new ConnectResponse(diffMsg.length(), diffMsg.getBytes(), false);

  }

  @Test
  public void isSuccess() {
    Assert.assertEquals(true, cr1.isSuccess());
  }

  @Test
  public void getMsgSize() {
    Assert.assertEquals(29, cr1.getMsgSize());

  }

  @Test
  public void getMsg() {
    byte[] msgCheck = message.getBytes();
    Assert.assertEquals(msgCheck.length, cr1.getMsg().length);
    for(int i = 0; i < msgCheck.length; i++) {
      Assert.assertEquals(msgCheck[i], cr1.getMsg()[i]);
    }
  }

  @Test
  public void getStringMsg() {
    Assert.assertEquals(message, cr1.getStringMsg());
  }

  @Test
  public void equalsTrue() {
    Assert.assertTrue(cr1.equals(cr2));
  }

  @Test
  public void equalsFalse() {
    Assert.assertFalse(cr1.equals(cr3));
  }

  @Test
  public void equalsFalseObject() {
    Assert.assertFalse(cr1.equals("hello"));
  }

  @Test
  public void hashCodeTest() {
    Assert.assertEquals(cr1.hashCode(), cr2.hashCode());
  }

  @Test
  public void toStringTest() {
    String expect = "ConnectResponse{success=true} Response{msgSize=29, msg=[84, 104, 101, 32, 99, 111, 110, 110, 101, 99, 116, 105, 111, 110, 32, 119, 97, 115, 32, 115, 117, 99, 99, 101, 115, 115, 102, 117, 108]} Communication{type=CONNECT_RESPONSE}";
    Assert.assertEquals(expect, cr1.toString());
  }

}