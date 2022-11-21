import java.util.ArrayList;
import java.util.Collection;

public class ServerLogic
{
    private MessageHandler message;

    private final String OptionsMin = "1. Register\n2. Login\n";
    private final String OptionsFull = OptionsMin + "3. Add Bug\n4. Assign Bug\n5. View all Bugs\n6. Update bug\n";
    private final String WelcomeMessageStart = "\n\nWelcome to the Bug Tracker";
    private final String WelcomeMessageEnd = "\nplease enter the number for the\nmenu you wish to access\n";

    private Collection<User> users = new ArrayList<>();

    private Boolean loggedin = false;
    private String name = "";

    public ServerLogic(MessageHandler message)
    {
        this.message = message;
    }

    // All the server logic
    public void run()
    {
        // 1. Send Welcome Message
        message.sendString(WelcomeMessageStart + name + WelcomeMessageEnd);

        // 2. Menu options List 1-2
        if (loggedin)
        {
            message.sendString(OptionsFull + "\nInput number 1-6");
        }
        else
        {
            message.sendString(OptionsMin + "\nInput number 1-2");
        }

        // 3. Send a request to the client asking to choose a menu option
        int menuOption = message.requestNumber("> ");

        // 4. Send bool if error
        if (menuOption < 1 || menuOption > (loggedin ? 6 : 2))
        {
            System.out.println("Error");
            message.sendBoolean(true);// Error send client back to start
            return;
        }
        message.sendBoolean(false);// No Error

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
        Boolean validLogin = false;

        do
        {
            message.sendString("Enter your email to login");
            var email = message.requestString("Email: ");

            for (User user : users)
            {
                if (email.equalsIgnoreCase(user.getEmail()))
                {
                    name = " " + user.getName();
                    validLogin = true;
                    loggedin = true;
                }
            }

            message.sendBoolean(validLogin);

        } while (!validLogin);
    }

    private void register()
    {
        Boolean added = false;
        // Request User info for registration
        do
        {
            String name = message.requestString("Name: ");
            int id = message.requestNumber("ID: ");
            String email = message.requestString("Email: ");
            String department = message.requestString("Department: ");

            added = addNewUser(name, id, email, department);
        } while (!added);
    }

    private Boolean addNewUser(String name, int id, String email, String department)
    {
        User newUser = new User(name, id, email, department);

        for (User user : users)
        {
            if (newUser.getEmail().equalsIgnoreCase(user.getEmail()))
            {
                return false;
            }
            if (newUser.getId() == user.getId())
            {
                return false;
            }
        }

        System.out.println("Added new user\n    " + name + "," + id + "," + email + "," + department);
        users.add(newUser);
        return true;
    }
}
