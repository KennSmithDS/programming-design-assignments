# Assignment 6 - Build a Chatroom App

In this assignment, we were tasked with making a simple messaging app, which consists of client(s) and a server. As the assignment specs indicate, our solution contains of a single server which manages clients such that they can communicate and message each other. Further, there are some more commands that the chatroom must support, such as showing all the users connected, sending broadcast messages (message sent to all connected users), send direct message, and query users (see what users are connected). The server must also manage the chatroom such that there is a limited number of users that can be connected at a certain time, and manage connecting and disconnecting these users. Finally, the chatroom app must adhere to a protocol of messages and responses.


## Classes and Key Methods

**CLASS: Communication:**
The communication package is where all the classes representing the chatroom protocol are held. We chose to implement abstract classes as well as an enum to set up the protocol (i.e. to distinguish one protocol type from another), and then non-abstract classes extend these abstract parent classes. 
- `communicationFactory` **method**: This is the primary/most relevant method in the communication class. It takes in a String as input, which contains all the information necessary to build a Communication object subtype (i.e. to create a DirectMessage object). If the string is not formatted correctly, then the method will throw an InvalidMessageException. The method parses the input string, and first begins by checking the integer at the start of the string to figure out what kind of communication we are constructing (as per the protocol in the specs, for example CONNECT_MESSAGE has identifier 19). The method will then assign the correct Identifier enum to the communication object, and then from the rest of the parsed string, populate any other fields.

**CLASS: Message:**
The Message class is a child class of the Communication class. The Message class itself is also an abtract class. We chose to split the types of protocol/communication into two different subtypes, being Response (see description for Response class below) as well a this Message class in order to minimize code duplication, since the Message subtypes all have a sender username (byte[]) and a sender username length, whereas every Response has a message (byte[]) and message length. Subclasses of both Response and Message might have additional fields, but we minimize code duplication for fields that are the same across them all.
- `getStringName` **method**: This method returns a string representation of the byte array username.
- `Getter` **methods**: We also have getter methods for the username size and byte array representation of username.
- <ins>Message types that extend Message.class</ins>:
  - CONNECT_MESSAGE(19)
  - DISCONNECT_MESSAGE(21)
  - QUERY_USERS(23)
  - BROADCAST_MESSAGE(25)
  - DIRECT_MESSAGE(26)
  - SEND_INSULT(28)

**CLASS: Response:**
The Response class, as mentioned above, as another abtract class that extends the Communication class.

