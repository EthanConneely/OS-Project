
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
