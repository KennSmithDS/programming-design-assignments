import static org.junit.Assert.*;

import Communications.DisconnectResponse;
import org.junit.Before;
import org.junit.Test;

public class DisconnectResponseTest {

  private String user = "isidorac";
  private DisconnectResponse dr;

  @Before
  @Test
  public void setUp() throws Exception {
    dr = new DisconnectResponse(user.length(), user.getBytes());
  }
}