public class ClientLogic
{
    private MessageHandler message;

    public ClientLogic(MessageHandler message)
    {
        this.message = message;
    }

    // All the server logic
    public void Run()
    {
        // Print Welcome Message
        System.out.println(message.readString());

        // Print Menu List 1-2
        System.out.println(message.readString());

        // Handle Menu on client
        int menuOption = message.handleRequestNumber();
        handleMenu(menuOption);
    }

    private void handleMenu(int menuOption)
    {
        switch (menuOption)
        {
        case 1:
            registration();
            break;

        case 2:
            login();
            break;

        case 3:
            break;

        case 4:
            break;

        case 5:
            break;

        case 6:
            break;

        }
    }

    private void login()
    {
        do
        {
            System.out.println(message.readString());
            message.handleRequestString();// Respond to email request

        } while (message.readBoolean());
    }

    private void registration()
    {
        message.handleRequestString(); // Name
        message.handleRequestNumber(); // ID
        message.handleRequestString(); // Email
        message.handleRequestString(); // Department
    }
}
