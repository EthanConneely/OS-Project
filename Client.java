import java.io.*;
import java.net.*;

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
		socket = new Socket();

		handleConnectionLoop();

		try
		{
			System.out.println("Connected to localhost in port 2004");

			// 2. get Input and Output streams
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());

			MessageHandler message = new MessageHandler(out, in);
			ClientLogic logic = new ClientLogic(message);
			while (true)
			{
				logic.Run();
			}
		}
		catch (UnknownHostException unknownHost)
		{
			System.out.println("You are trying to connect to an unknown host!");
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

	private void handleConnectionLoop()
	{
		int count = 0;

		// Run a loop trying to connect to server
		do
		{
			try
			{
				socket.connect(new InetSocketAddress("localhost", 2004), 500);
			}
			catch (Exception e)
			{
			}

			if (count > 0)
			{
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				System.out.println("Reconnection Attempt - " + count);
			}

			count++;
		} while (!socket.isConnected());
	}

	public static void main(String args[])
	{
		System.out.println("\n\n-- Client --\n\n");

		// Create and run the client
		Client client = new Client();
		client.run();
	}
}
