
SETUP: 
(SERVER SIDE)
The server must be compiled and ran first. To use default port simply run the server with
the command line empty. If the server user wishes to use an optional port the server user can 
input '-csp' with an optional port next to it. The valid port numbers that the server will accept
is 14001<= serverPort <= 60000 (this was arbritrary). 

(CLIENT)
Once the server is setup, the client can join the server simply by compiling and running it.
If no optional port number and IP address are inputted in the command line, the client will 
automatically run with the default port 14001 and the default IP 'localhost'. If the client
inputs an invalid port number, the client will be asked to input another one. '-ccp' and '-cca' 
will allow clients to input optional port and address e.g. '-cca' 192.168.10.250 '-ccp' 14002 will
try to connect the client to server port 14002 with address 192.168.10.250

(CHATBOT)
The chatbot will only render useful when both client and server sides are running. The chatbot
acts as a client therefore have similar functionailities with some added features. If the chatbot is
connected to the server, the user will receive bot responses. If the user wishes to hear a joke, by typing 'joke',
the bot will respond with a joke. If the client wishes to hear a fact, by typing 'fact, the bot
will respond with a fact.


BROADCASTING:
(SERVER)
The server can recieve messages from every client that joined. Once recieved it broadcasts all messages
to every client. The clients can then recieve message even before they start typing via multithreading.

(CHATBOT)
If the chatbot joins, the server thread will know and handle bot responses. Once the chatbot leaves/
disconnect, the server will aknowledge this and revert back to normal responses, even if the user
types 'joke' or 'fact'. 


DISCONNECTIONS:
The server user can shutdown the server by simply typing 'EXIT'. When the server shutsdown, the client
will receive the message 'Server has shut down'. The server socket will close as well as the every client/bot
sockets that is connected and the client's buffered reader.


ADDED FEATURE:
(CLIENT ID)
Each client will have a unique ID, starting from 1 to whoever joins, and is incremented by 1 when a new client
joins. The client can then view its own messages and differentiate itself in the chat.

