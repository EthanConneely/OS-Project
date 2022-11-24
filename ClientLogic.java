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
        // 0.1. Print Welcome Message
        System.out.println(message.readString());

        // 0.2. Print Menu List
        System.out.println(message.readString());

        // 0.3. Handle Menu on client
        int menuOption = message.handleRequestRangedNumber();

        handleMenu(menuOption);
    }

    private void handleMenu(int menuOption)
    {
        switch (menuOption)
        {
        case 1:
            register();
            break;

        case 2:
            login();
            break;

        case 3:
            addBug();
            break;

        case 4:
            break;

        case 5:
            break;

        case 6:
            break;

        }
    }

    private void addBug()
    {
        message.handleRequestString(); // 3.1. AppName
        message.handleRequestString(); // 3.2. DateTime
        message.handleRequestRangedNumber(); // 3.3. Platform
        message.handleRequestString(); // 3.4. Description
        message.handleRequestNumber(); // 3.5. Status
    }

    private void login()
    {
        Boolean validLogin = false;

        do
        {
            // 1.1. Recieve text to login
            System.out.println(message.readString());

            // 1.2. Recieve request for email Prompt
            message.handleRequestString();

            // 1.3. Recieve error if we sent an invalid input
            validLogin = message.readBoolean();
        } while (!validLogin);
    }

    private void register()
    {
        message.handleRequestString(); // 2.1. Name
        message.handleRequestNumber(); // 2.1. ID
        message.handleRequestString(); // 2.1. Email
        message.handleRequestString(); // 2.1. Department

        System.out.println(message.readString());
    }
}
