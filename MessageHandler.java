import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Scanner;

import Database.Platform;
import Database.Status;

public class MessageHandler
{
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Scanner scanner;

    public MessageHandler(Socket socket)
    {
        try
        {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.out.flush();
            this.in = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException e)
        {
            System.out.println("MessageHandler Wrong Object Read");
        }

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
            System.out.println("sendString Wrong Object Read");
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
            System.out.println("sendNumber Wrong Object Read");
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
            System.out.println("sendBoolean Wrong Object Read");
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
            System.out.println("readString " + e.getMessage());
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
            System.out.println("readNumber Wrong Object Read");
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
            System.out.println("readBoolean Wrong Object Read");
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

    public String requestDate(String prompt)
    {
        String result = "";
        Boolean invalidInput = true;
        while (invalidInput)
        {
            result = requestString(prompt);

            invalidInput = false;
            try
            {
                LocalDateTime.parse(result);
            }
            catch (Exception e)
            {
                invalidInput = true;
            }
            sendBoolean(invalidInput);
        }
        return result;
    }

    /**
     * Requests a number in a range rerequesting it if the user sends a wrong number
     *
     * @param min    Range inclusive
     * @param max    Range inclusive
     * @param prompt The prompt to show the client
     * @return Returns only the valid number the user has sent back
     */
    public int requestNumberRange(int min, int max, String prompt)
    {
        int result = -1;
        Boolean invalidInput = true;
        while (invalidInput)
        {
            result = requestNumber(prompt);

            invalidInput = result < min || result > max;

            sendBoolean(invalidInput);
        }
        return result;
    }

    public int handleRequestNumberRange()
    {
        int result = 0;
        do
        {
            result = handleRequestNumber();

            // 0.4. Check bool for error
        } while (readBoolean());
        return result;
    }

    public String handleRequestDate()
    {
        String result = "";
        do
        {
            result = handleRequestString();

            // 0.4. Check bool for error
        } while (readBoolean());
        return result;
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
     * Request a string from the user
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
