
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import Database.Platform;
import Database.Status;

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
        catch (Exception ioException)
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
        catch (Exception ioException)
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
        catch (Exception ioException)
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
        catch (Exception e)
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
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Read a bool sent over the network
     */
    public Boolean readBoolean()
    {
        try
        {
            return (Boolean) in.readObject();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
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

    /**
     * Requests a number in a range rerequesting it if the user sends a wrong number
     *
     * @param min    Range inclusive
     * @param max    Range inclusive
     * @param prompt The prompt to show the client
     * @return Returns only the valid number the user has sent back
     */
    public int requestRangedNumber(int min, int max, String prompt)
    {
        int menuOption = -1;
        Boolean invalidInput = true;
        while (invalidInput)
        {
            menuOption = requestNumber(prompt);

            invalidInput = menuOption < min || menuOption > max;
            System.out.println(invalidInput);
            sendBoolean(invalidInput);
        }
        return menuOption;
    }

    public int handleRequestRangedNumber()
    {
        int menuOption = 0;
        do
        {
            menuOption = handleRequestNumber();

            // 0.4. Check bool for error
        } while (readBoolean());
        return menuOption;
    }

    public int handleRequestNumber()
    {
        System.out.print(readString());

        int response;
        try
        {
            response = Integer.parseInt(scanner.nextLine());
        }
        catch (Exception e)
        {
            response = -1;
        }
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

    public Platform handleRequestPlatformEnum()
    {
        System.out.print(readString());

        return Platform.valueOf(readString());
    }

    public Status handleRequestStatusEnum()
    {
        System.out.print(readString());

        return Status.valueOf(readString());
    }
}
