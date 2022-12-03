import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientRoom {
    private int port;
    protected ArrayList<Connection> connections = new ArrayList<>();
    protected HashMap<String, Connection> userNames = new HashMap<>();


    public ClientRoom(int port) {
        this.port = port;
    }

    //Associates a username to a connection
    void addUserName(String userName, Connection connection) {
        userNames.put(userName, connection);
    }

    public void broadcast_msg(String msg, String emitter) {
        var origin = userNames.get(emitter);
        for(Connection c : connections) {
            if(c != origin) c.send(msg);
        }
    }

    public void execute() {
        // try to open a server socket on port args[0] (> 1024)
        try (ServerSocket serv = new ServerSocket(port)) {

            System.out.println("Server launched - listening port: " + port);

            while(true) {

                System.out.println("waiting for client...");
                // accept client connection request
                Socket client = serv.accept();

                Connection connection = new Connection(client, this);
                //adds the new connection to the arrayList
                this.connections.add(connection);
                connection.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        ClientRoom clientRoom = new ClientRoom(port);
        clientRoom.execute();
    }
}


