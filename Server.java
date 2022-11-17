import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public Server()
	{
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
					System.out.println("Connection received from " + socket.getInetAddress().getHostName());

					out = new ObjectOutputStream(socket.getOutputStream());
					out.flush();
					in = new ObjectInputStream(socket.getInputStream());

					MessageHandler message = new MessageHandler(out, in);
					ServerLogic logic = new ServerLogic(message);
					while (true)
					{
						logic.run();
					}
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
