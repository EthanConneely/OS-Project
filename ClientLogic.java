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
        int menuOption = message.handleNumberFailable();

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
            assignBug();
            break;

            case 5:
            viewAllBugs();
            break;

            case 6:
            updateBug();
            break;

        }
    }

    private void updateBug()
    {
        message.handleNumberFailable(); // 6.1. BugID
        message.handleNumberFailable(); // 6.2. New bug status
    }

    private void viewAllBugs()
    {
        var bugs = message.readString();
        System.out.println(bugs);
    }

    private void assignBug()
    {
        message.handleNumberFailable();// 4.1. BugID
        message.handleNumberFailable("A user does not exsist with that id!");// 4.2. UserID to assign
    }

    private void addBug()
    {
        message.handleString(); // 3.1. AppName
        message.handleDate(); // 3.2. DateTime
        message.handleNumberFailable(); // 3.3. Platform
        message.handleString(); // 3.4. Description
        message.handleNumberFailable(); // 3.5. Status
    }

    private void login()
    {
        // 2.1. Recieve text to login
        System.out.println(message.readString());

        // 2.2. Recieve request for email Prompt
        message.handleString();

        // 2.3. Recieve request for ID Prompt
        message.handleNumberFailable();

        // 2.4. Recieve error if we sent an invalid input
        Boolean validLogin = message.readBoolean();

        if (!validLogin)
        {
            System.out.println("Error with login try again!");
        }
    }

    private void register()
    {
        message.handleString(); // 1.1. Name
        message.handleNumber(); // 1.2. ID
        message.handleString(); // 1.3. Email
        message.handleString(); // 1.4. Department

        System.out.println(message.readString());
    }
}
