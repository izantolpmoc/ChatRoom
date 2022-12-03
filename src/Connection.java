import java.io.*;
import java.net.Socket;

public class Connection extends Thread{
    Socket socket;
    ClientRoom clientRoom;
    BufferedReader inchan;
    DataOutputStream outchan;


    public Connection(Socket s, ClientRoom c) {
        try {
            socket =s;
            clientRoom =c;
            inchan = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outchan = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    @Override
    public void run() {

        try {
            outchan.writeBytes("Please enter your name or alias: ");
            String userName = inchan.readLine();

            //Associates username with new connection
            clientRoom.addUserName(userName, this);

            //Lets users know a new user has joined the chat
            clientRoom.broadcast_msg("New user connected: " + userName, userName);
            System.out.println("New user connected: " + userName);

            //displays usernames of currently active connections
            showConnectedUsers();

            while (true) {

                String command = inchan.readLine();
                System.out.println(">>> message received: " + command);
                // close the connection when receiving ""
                if (command.equals("")) {
                    //lets users know this user is no longer available
                    clientRoom.broadcast_msg(userName + " has left the chat.", userName);
                    outchan.writeBytes("You left the chat.");

                    //removes the connexion from the lists
                    clientRoom.userNames.remove(userName);
                    clientRoom.connections.remove(this);
                    System.out.println(userName + " quit.");
                    break;
                }
                //shows username before the message
                var formattedMsg = userName + ": " + command;
                clientRoom.broadcast_msg(formattedMsg + "\n", userName);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        try {
            outchan.writeBytes(message + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showConnectedUsers() {
        try {
            var connectedUsers = clientRoom.userNames.keySet().size() > 1 ?
                    clientRoom.userNames.keySet()
                    : "No one else is connected.";
            outchan.writeBytes("Connected users (including you): " + connectedUsers + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void interrupt () {
        super.interrupt ();
        try {
            socket.close ();
        } catch (IOException e) {} // quietly close
    }
}
