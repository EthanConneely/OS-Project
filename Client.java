
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client
{
    private Socket socket;

    Client()
    {
    }

    void run()
    {
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        // Create the socket we use to connect to the server
        try
        {
            socket = new Socket("127.0.0.1", 2004);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }

        try
        {
            System.out.println("Connected to localhost in port 2004");

            MessageHandler message = new MessageHandler(socket);
            ClientLogic logic = new ClientLogic(message);
            while (true)
            {
                logic.Run();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                in.close();
                out.close();
                socket.close();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
        }
    }

    public static void main(String args[])
    {
        System.out.println("\n\n-- Client --\n\n");

        try
        {
            Thread.sleep(250);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        // Create and run the client
        Client client = new Client();
        client.run();
    }
}
