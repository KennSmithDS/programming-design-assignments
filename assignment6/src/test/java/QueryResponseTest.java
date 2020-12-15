import static org.junit.Assert.*;

import Communications.QueryResponse;
import java.util.HashMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class QueryResponseTest {

  private QueryResponse qr1;
  private QueryResponse qr2;
  private QueryResponse qr3;
  private String user1 = "isidorac";
  private String user2 = "kendall";
  private String user3 = "filip_con";
  private String user4 = "maia.shen";
  private byte[] user1Bytes = user1.getBytes();
  private byte[] user2Bytes = user2.getBytes();
  private HashMap<byte[], Integer> users1;
  private HashMap<byte[], Integer> users2;


  @Before
  public void setUp() throws Exception {

    users1 = new HashMap<>();
    users1.put(user1Bytes, user1.length());
    users1.put(user2Bytes, user2.length());

    users2 = new HashMap<>();
    users2.put(user3.getBytes(), user3.length());
    users2.put(user4.getBytes(), user4.length());

    qr1 = new QueryResponse(2, users1);
    qr2 = new QueryResponse(2, users1);
    qr3 = new QueryResponse(2, users2);

  }

  @Test
  public void getNumUsers() {
    Assert.assertEquals(2, qr1.getNumUsers());
  }

  @Test
  public void getUsers() {
    HashMap<byte[], Integer> users = qr1.getUsers();
    Assert.assertEquals(2, users.size());
    Assert.assertTrue(users.containsKey(user1Bytes));
    Assert.assertTrue(users.containsKey(user2Bytes));
    Assert.assertTrue(users.containsValue(user1.length()));
    Assert.assertTrue(users.containsValue(user2.length()));
  }

  @Test
  public void equalsTrue() {
    Assert.assertTrue(qr1.equals(qr2));
  }

  @Test
  public void equalsFalse() {
    Assert.assertFalse(qr1.equals(qr3));
  }

  @Test
  public void equalsFalseObject() {
    Assert.assertFalse(qr1.equals("hello"));
  }

  @Test
  public void hashCodeTest() {
    Assert.assertEquals(qr1.hashCode(), qr1.hashCode());
  }


}