# Assignment 6 - Build a Chatroom App

In this assignment, we were tasked with making a simple messaging app, which consists of client(s) and a server. As the assignment specs indicate, our solution contains of a single server which manages clients such that they can communicate and message each other. Further, there are some more commands that the chatroom must support, such as showing all the users connected, sending broadcast messages (message sent to all connected users), send direct message, and query users (see what users are connected). The server must also manage the chatroom such that there is a limited number of users that can be connected at a certain time, and manage connecting and disconnecting these users. Finally, the chatroom app must adhere to a protocol of messages and responses.

## Usage

In order to run this application, you must open the terminal to first run the Server before any client. 
```
java -jar ChatServerApp.jar
```
For every subsequent user/client which wants to log on to the app, we must open a new terminal window, and run the following command:
```
java -jar ChatClientApp.jar
```
Then, for every client terminal, you may begin to pass command line arguments to send messages, query users, etc.


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
  - QUERY_CONNECTED_USERS(23)
  - BROADCAST_MESSAGE(25)
  - DIRECT_MESSAGE(26)
  - SEND_INSULT(28)

**CLASS: Response:**
The Response class, as mentioned above, as another abtract class that extends the Communication class, and represents response types in the protocol (which will extend this Response class).
- `getStringMsg` **method**: This method returns a string representation of the byte array message.
- `Getter` **methods**: We also have getter methods for the message size and byte array representation of the message.
- <ins>Message types that extend Message.class</ins>:
  - CONNECT_RESPONSE(20)
  - DISCONNECT_RESPONSE(22)
  - QUERY_USER_RESPONSE(24)
  - FAILED_MESSAGE(27)
  
**CLASS: ConnectMessage:**
This is a (non-abstract) class that represents a CONNECT_MESSAGE(19) in the protocol, and extends the Message parent (abstract) class. It has no additional fields other than those that it inherits from the parent Message class, and thus has no additional methods.

**CLASS: ConnectResponse:**
This is a (non-abstract) class that represents a CONNECT_RESPONSE(20) in the protocol, and extends the Response parent (abstract) class. It has a single additional filed apart from those in the parent Response class, which is a boolean `success` representing whether or not the connection was successful.
- `isSuccess` **method**: This method returns a boolean which represents the success (true = successful, false = unsuccessful) connection.

**CLASS: DisconnectMessage:**
This is a (non-abstract) class that represents a DISCONNECT_MESSAGE(21) in the protocol, and extends the Message parent (abstract) class. It has no additional fields other than those that it inherits from the parent Message class, and thus has no additional methods.

**CLASS: DisconnectResponse:**
This is a (non-abstract) class that represents a DISCONNECT_RESPONSE(22) in the protocol, and extends the Response parent (abstract) class. It has no additional fields other than those that it inherits from the parent Response class, and thus has no additional methods.

**CLASS: QueryUsers:**
This is a (non-abstract) class that represents a QUERY_CONNECTED_USERS(23) in the protocol, and extends the Message parent (abstract) class. It has no additional fields other than those that it inherits from the parent Message class, and thus has no additional methods.

**CLASS: QueryResponse:**
This is a (non-abstract) class that represents a DISCONNECT_RESPONSE(22) in the protocol, and extends the RESPONSE parent (abstract) class. It has additional fields other than those that it inherits from the parent Response class; an int representing the number of connected users, as well as a HashMap<byte[], Integer> which has key = byte array username of each user, and value = int length of the username. This way, we can have an object which holds all the users we need to show to the user requesting it. 
- `getNumUsers` **method**: This method returns an integer of how many users are connected.
- `printUsers` **method**: This method prints the string representation of the username of all users present in the hashmap.
- `getUsers` **method**: This method returns the hashmap of users.

**CLASS: BroadcastMessage:**
This is a (non-abstract) class that represents a BROADCAST_MESSAGE(25) in the protocol, and extends the Message parent (abstract) class. It has additional fields outside those it inherits from the parent Message class; an int representing the length of the message, as well as a byte array representing the message itself.
- `getMsgSize` **method**: This method returns an integer size of the message to be broadcast.
- `getMsg` **method**: Returns the byte array represnting the message to be broadcast.
- `getStringMsg` **method**: This method returns a string representation of the message to be broadcast.

**CLASS: DirectMessage:**
This is a (non-abstract) class that represents a DIRECT_MESSAGE(26) in the protocol, and extends the Message parent (abstract) class. It has additional fields outside those it inherits from the parent Message class; an int representing the length of the recipient username, a byte array representing the recipient username, an int representing the length of the message, as well as a byte array representing the message itself.
- `getMsgSize` **method**: This method returns an integer size of the message to be broadcast.
- `getMsg` **method**: Returns the byte array represnting the message to be broadcast.
- `getStringMsg` **method**: This method returns a string representation of the message to be broadcast.
- `getRecipNameSize` **method**: This method returns an integer length of the recipient's username.
- `getRecipUsername` **method**: This method returns a byte array represnting the recipient's username.
- `getRecipStringName` **method**: This method returnsThis method returns a string representation of the recipient's username.

**CLASS: FailedResponse:**
This is a (non-abstract) class that represents a FAILED_MESSAGE(27) in the protocol, and extends the Response parent (abstract) class. It has no additional fields other than those that it inherits from the parent Response class, and thus has no additional methods.

**CLASS: InsultMessage:**
This is a (non-abstract) class that represents a SEND_INSULT(28) in the protocol, and extends the Message parent (abstract) class. It has additional fields outside those it inherits from the parent Message class; an int representing the length of the recipient username and a byte array representing the recipient username.
- `getRecipNameSize` **method**: This method returns an integer length of the recipient's username.
- `getRecipUsername` **method**: This method returns a byte array represnting the recipient's username.
- `getRecipStringName` **method**: This method returnsThis method returns a string representation of the recipient's username.

**CLASS: Server:**
This is the class that respresents a Server. As its fields, it has a server socket, server port, ConcurrentHashMap holding all the ClientSessions (see class description below), boolean representing if the server is running, and findally a treadpool (of fixed size 10) which holds all the ClientSession threads (again, see description for ClientSession below). This class manages all the users (represented as ClientSession objects) through its threadpool, and thus can manage how many users we have connected. It accepts or declines client requests to join the chatroom.
- `Main` **method**: Gets the socket, and then keeps track of how many users there are and if they try to connect, either accepts (if there is room in the threadpool), or not otherwise.
- `acceptClientRequest` **method**: Helper method for the main method, which accepts a user and creates a new ClientSession object represnting that user, creates a new ClientSession thread, and adds it to the threadpool + executes.
- `Getter` **methods**: We have various getter methods for the different fields mentioned above.

**CLASS: Client:**
This is the class that is the user interface (UI) fo the clients/users. It reads the console for input from the users, and depending on the user input, then creates the corresponding Communication object to send to the server (through ServerConnection object, see below). While reading from the console (i.e. reading user input), this class makes sure that the user doesn't break any edge cases; cannot logoff before being logged on, cannot logon if already logged on, etc. by keeping track of the ServerConnection status (see below). The Client object communicates across the chatroom through its associated ServerConnection object, which has an inputObjectStream and OutputObjectStream. Thus, when the Client main method reads a command from the commandline, it will construct the appropriate Communication object and then it across the outputObjectStream.
- `listenForUserCommands` **method**: This method reads from the commandline (reads the user's commands), and checks them (edge cases mentioned above), constructs the appropriate Communication object corresponding to the command, and then puts it onto the ServerConnection object's ObjectOutputStream to be sent to the redirected appropriately.
- `Main` **method**: Instantiates the ServerConnection object, and then uses the `listenForUserCommands` helper method to read commandline input and send out Communication objects.

**CLASS: ServerConnection:**
This is the class serves the connection between a particular Client UI and the rest of the chatroom app. ServerConnection implements runnable, and has booleans represnting "connected" and "allowLogoff", which handle edge cases mentioned above. It also has a socket, and ObjectInputStream through which it reads messages (Communication objects) sent by the client UI. When it receives a message from the client or ClientSession (handles Communications from other users/Server over the server connection), it will print that Communication to the console which is seen by the client UI.
- `handleServerConnection` **method**: This is the most important method in the ServerConnection class, and is called in its run() method (because it implements runnable). In this method, the ServerConnection will read from its ObjectInputStream, check what kind of Communication it is, and then print to the client console the appropriate information contained in that Communication. Further, for received InsultMessage objects, this method will generate the random insult and print that to the client console.
- `isConnected` **method**: Returns boolean if we have connected to the server.
- `isAllowLogoff` **method**: Returns boolean if we can logoff (i.e. we only allow logging off once we have connected).

**CLASS: ClientSession:**
This class also implements Runnable, and thus is a class of objects which represnt a single user, that we can then add to the server threadpool. Its main responsability is to route messages between the clients and generate Communication objects in response to either a Communication from another user or an error (FailMessage). This object also has a socket, server (Server class), port, ObjectInputStream, ObjectOutputStream, and a boolean representing if the session is connected to the server. 
- `handleClientSession` **method**: This is the most important method in the ClientSession class. From the socket, it gets an input and output stream, through which it sends and receives Communications. Depending on what kind of Communication it receives, it sends an appropriate Communication in response, which is read by the corresponding user's ServerConnection. For example, if it receives a ConnectMessage, it will generate a ConnectResponse and then send that on the output stream which will be read by the ServerConnection and printed/handled to the user UI.
- `sendDirectMessage` **method**: Helper method for `handleClientSession`, such that it will check if the direct message recipient exists, and if they do, it will redirect and send the direct message. If the user doesn't exist, it will instead respond with a FailedResponse Communication.
- `sendInsult` **method**: Helper method for `handleClientSession`, such that it will check if the insult message recipient exists, and if they do, it will redirect and send the insult message. If the user doesn't exist, it will instead respond with a FailedResponse Communication.
- `sendFailedMessage` **method**: Helper method for `handleClientSession`, which constructs a FailedResponse object and send this across the output stream.
- `sendBroadcastMessage` **method**: Helper method for `handleClientSession`, which will get the HashMap of active ClientSessions from the Server object, and then send out the message to each of the connected users.
- `send` **methods**: More helper methods for `handleClientSession`, which send differen types of messages/Communications. These all function in the same way as the helper methods mentioned above, but for different types of messages (all use the communicationFactory method to construct the messages, such as:
  - `sendConnectionResponse` **method**: Sends an ConnectionResponse Communication object.
  - `sendDisconnectionResponse` **method**: Sends an DisonnectionResponse Communication object.
  - `sendUserQueryResponse` **method**: Sends an QueryResponse Communication object.
  
## Edge Cases and Assumptions

1) Cannot send message or logoff before logging on. 
2) Cannot have two users with the same name.
3) Can only have 10 clients/users at a time.
4) Assume that users must log off before the Server is terminated/closed.

## Note on Issues
We ran into some problems when testing our assignment. Testing the Communication classes (and subclasses) was not a problem, however, we struggled with testing the Client, Server, ClientSession, and ServerConnection classes. These were particlarly difficult to test due to the combination of port/socket handling and multi threads to handle and test. Further, for the Client class, we also would need to emmulate the commandline input, as well as multithreads and sockets due to the way that our assignment was structured. We kept running into different errors when trying to establish a socket connection in the test, and could not find an appropriate way/solution to set up the objects of these classes such that we could test them. We tried some solutions such as:

- In order to overcome threading issues in the Server class tests, we made a random port generator so that we would not run into multi thread issues when testing different aspects of Server. However, since Server runs in the Main method, we could not kill the thread which we believe was causing some issues.


