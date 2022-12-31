import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Scanner;

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
            System.out.println("MessageHandler " + e.getMessage());
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
        catch (Exception e)
        {
            System.out.println("sendString " + e.getMessage());
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
        catch (Exception e)
        {
            System.out.println("sendNumber " + e.getMessage());
        }
    }

    /**
     * Send Boolean over the network
     */
    public void sendBoolean(Boolean state)
    {
        try
        {
            out.writeObject(state);
            out.flush();
        }
        catch (Exception e)
        {
            System.out.println("sendBoolean " + e.getMessage());
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
            System.out.println("readNumber " + e.getMessage());
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
            System.out.println("readBoolean " + e.getMessage());
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

    public LocalDateTime requestDate(String prompt)
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
        return LocalDateTime.parse(result);
    }

    /**
     * Requests a number in a range rerequesting it if the user sends a wrong number
     *
     * @param min    Range inclusive
     * @param max    Range inclusive
     * @param prompt The prompt to show the client
     * @return Returns only the valid number the user has sent back
     */
    public int requestNumber(String prompt, int min, int max)
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

    public int handleNumberFailable()
    {
        int result = 0;
        Boolean failed = true;
        while (failed)
        {
            result = handleNumber();
            failed = readBoolean();
        }
        return result;
    }

    public int handleNumberFailable(String m)
    {
        int result = 0;
        Boolean failed = true;
        while (failed)
        {
            result = handleNumber();
            failed = readBoolean();
            if (failed)
            {
                System.out.println(m);
            }
        }
        return result;
    }

    public String handleDate()
    {
        String result = "";
        do
        {
            result = handleString();

            // 0.4. Check bool for error
        } while (readBoolean());
        return result;
    }

    public int handleNumber()
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
    public String handleString()
    {
        System.out.print(readString());

        String response = scanner.nextLine();
        sendString(response);
        return response;
    }
}
