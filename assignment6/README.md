# Assignment 6 - Build a Chatroom App

In this assignment, we were tasked with making a simple messaging app, which consists of client(s) and a server. As the assignment specs indicate, our solution contains of a single server which manages clients such that they can communicate and message each other. Further, there are some more commands that the chatroom must support, such as showing all the users connected, sending broadcast messages (message sent to all connected users), send direct message, and query users (see what users are connected). The server must also manage the chatroom such that there is a limited number of users that can be connected at a certain time, and manage connecting and disconnecting these users. Finally, the chatroom app must adhere to a protocol of messages and responses.


## Classes and Key Methods

**COMMUNICATION:**
The communication package is where all the classes represnting the chatroom protocol are held. We chose to implement abstract classes as well as an enum to set up the protocol, and then non-abstract classes extend these abstract parent classes. Each class is listed below: 
- `Communication` **class**: This is the main abstract parent class. Every different element in the protocol is represented (inherits from) this class. We used a parent class so that we could then create a factory method for constructing messages and responses.
 - `main(String[] args)` **method**: Main method is the entry point to running the sequential solution, controlling both the CSVReader and CSVWriter classes.
- `CSVReader` **class**:

