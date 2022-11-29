public class Client2 extends Client
{
    public static void main(String args[])
    {
        System.out.println("\n\n-- Client2 --\n\n");

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
