import static org.junit.Assert.*;

import Communications.ConnectMessage;
import org.junit.Before;
import org.junit.Test;

public class ConnectMessageTest {

  private ConnectMessage cm;
  private String name = "isidorac";

  @Before
  @Test
  public void setUp() throws Exception {
    byte[] username = name.getBytes();
    int nameSize = name.length();
    cm = new ConnectMessage(nameSize, username);
  }


}