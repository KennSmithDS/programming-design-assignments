import static org.junit.Assert.*;

import Communications.QueryUsers;
import org.junit.Before;
import org.junit.Test;

public class QueryUsersTest {

  private String user = "isidorac";
  private QueryUsers qu;

  @Before
  @Test
  public void setUp() throws Exception {
    qu = new QueryUsers(user.length(), user.getBytes());
  }
}