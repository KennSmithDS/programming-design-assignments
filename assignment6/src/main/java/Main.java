import Communications.Communication;
import Communications.ConnectMessage;
import Communications.InvalidMessageException;

public class Main {

  public static void main(String[] args) throws InvalidMessageException {

    String name = "isidora";
    byte[] nameByte = name.getBytes();
    int size = name.length();

    ConnectMessage msg = new ConnectMessage(size, nameByte);

    if(msg instanceof Communications.ConnectMessage) {
      System.out.println("Hello!");
    }



    //System.out.println(msg.getClass());


  }

}
