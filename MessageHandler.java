import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class MessageHandler
{
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Scanner scanner;

    public MessageHandler(ObjectOutputStream out, ObjectInputStream in)
    {
        this.out = out;
        this.in = in;
        scanner = new Scanner(System.in);
    }

    public int requestNumber(String prompt)
    {
        sendString(prompt);
        return readNumber();
    }

    public String requestString(String prompt)
    {
        sendString(prompt);
        return readString();
    }

    public int handleRequestNumber()
    {
        System.out.print(readString());
        int response = Integer.parseInt(scanner.nextLine());
        sendNumber(response);
        return response;
    }

    /**
     *
     * @return What the user has input that is sent over the network
     */
    public String handleRequestString()
    {
        System.out.print(readString());
        String response = scanner.nextLine();
        sendString(response);
        return response;
    }

    /**
     * Send String over the network
     */
    public void sendString(String msg)
    {
        try
        {
            out.writeObject(msg);
            out.flush();
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    /**
     * Send int over the network
     */
    public void sendNumber(int msg)
    {
        try
        {
            out.writeObject(msg);
            out.flush();
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    /**
     * Send Boolean over the network
     */
    public void sendBoolean(Boolean msg)
    {
        try
        {
            out.writeObject(msg);
            out.flush();
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    /**
     * Read a string sent over the network
     */
    public String readString()
    {
        try
        {
            return (String) in.readObject();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Read an int sent over the network
     */
    public int readNumber()
    {
        try
        {
            return (int) in.readObject();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Read the boolean sent over the network
     */
    public Boolean readBoolean()
    {
        try
        {
            return (Boolean) in.readObject();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return false;
    }

}
