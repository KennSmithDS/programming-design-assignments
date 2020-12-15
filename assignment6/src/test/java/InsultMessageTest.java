import static org.junit.Assert.*;

import Communications.InsultMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InsultMessageTest {

  private InsultMessage im1;
  private InsultMessage im2;
  private InsultMessage im3;
  private String user1 = "isidorac";
  private String user2 = "kendall";
  private String recip1 = "filip_conic";
  private String recip2 = "maia.shen";

  @Before
  public void setUp() throws Exception {

    im1 = new InsultMessage(user1.length(), user1.getBytes(), recip1.length(), recip1.getBytes());
    im2 = new InsultMessage(user1.length(), user1.getBytes(), recip1.length(), recip1.getBytes());
    im3 = new InsultMessage(user2.length(), user2.getBytes(), recip2.length(), recip2.getBytes());

  }

  @Test
  public void getRecipNameSize() {
    Assert.assertEquals(recip1.length(), im1.getRecipNameSize());
  }

  @Test
  public void getRecipUsername() {
    byte[] recipByte = recip1.getBytes();
    Assert.assertEquals(recipByte.length, im1.getRecipUsername().length);
    for(int i = 0; i < recipByte.length; i++) {
      Assert.assertEquals(recipByte[i], im1.getRecipUsername()[i]);
    }
  }

  @Test
  public void getRecipStringName() {
    Assert.assertEquals(recip1, im1.getRecipStringName());
  }

  @Test
  public void equalsTrue() {
    Assert.assertTrue(im1.equals(im2));
  }

  @Test
  public void equalsFalse() {
    Assert.assertFalse(im1.equals(im3));
  }

  @Test
  public void equalsFalseObject() {
    Assert.assertFalse(im1.equals("hello"));
  }

  @Test
  public void hashCodeTest() {
    Assert.assertEquals(im1.hashCode(), im2.hashCode());
  }

  @Test
  public void toStringTest() {
    String expect = "InsultMessage{recipNameSize=11, recipUsername=[102, 105, 108, 105, 112, 95, 99, "
        + "111, 110, 105, 99]} Message{type=null, nameSize=8, username=[105, 115, 105, 100, 111, 114, 97, 99]}";
    Assert.assertEquals(expect, im1.toString());

  }
}