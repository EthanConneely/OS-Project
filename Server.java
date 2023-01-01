
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Database.Database;

public class Server
{
    private Socket socket;
    private Database database = new Database();

    public Server()
    {
        database.load();
    }

    public void run()
    {
        try (ServerSocket serverSocket = new ServerSocket(2004, 10))
        {
            try
            {
                while (true)
                {
                    System.out.println("Waiting for new connection");
                    socket = serverSocket.accept();
                    System.out.println("Connection received from " + socket.getInetAddress() + ":" + socket.getPort());

                    ServerLogic logic = new ServerLogic(socket, database);
                    logic.start();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                // Closing connection
                try
                {
                    serverSocket.close();
                    socket.close();
                }
                catch (IOException ioException)
                {
                    ioException.printStackTrace();
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String args[])
    {
        System.out.println("\n\n-- Server --\n\n");

        Server server = new Server();
        server.run();
    }
}
