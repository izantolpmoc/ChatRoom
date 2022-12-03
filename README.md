# ChatRoom
How does it work ?

Run the ClientRoom file with the port as a parameter (ex: 2000).
When the server is listening to new clients, use the command in a terminal to join the chat room: 
```bash 
ncat 127.0.0.1 2000
``` 

You will be asked your username and you will then be able to chat with other participants, if any.
The username of the user sending a message will be displayed.
If a new user joins or quits the chat, you will receive a notification. 
