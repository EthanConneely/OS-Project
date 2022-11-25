import java.net.Socket;
import java.time.LocalDateTime;

import Database.Database;
import Database.Platform;
import Database.Status;
import Database.User;

public class ServerLogic extends Thread
{
    private MessageHandler message;

    private final String OptionsMin = "1. Register\n2. Login\n";
    private final String OptionsFull = OptionsMin + "3. Add Bug\n4. Assign Bug\n5. View all Bugs\n6. Update bug\n";
    private final String WelcomeMessageStart = "\n\nWelcome to the Bug Tracker";
    private final String WelcomeMessageEnd = "\nPlease enter the number for the\nmenu you wish to access\n";

    private Database database;

    private Boolean loggedIn = false;
    private Boolean showWelcomeMessage = true;
    private String name = "";

    public ServerLogic(Socket socket, Database database)
    {
        this.message = new MessageHandler(socket);
        this.database = database;
    }

    @Override
    public void run()
    {
        while (true)
        {
            // 0.1. Send Welcome Message
            if (showWelcomeMessage)
            {
                message.sendString(WelcomeMessageStart + name + WelcomeMessageEnd);
            }
            else
            {
                message.sendString("");
            }

            showWelcomeMessage = false;

            // 0.2. Menu options List depending on if client is logged in
            if (loggedIn)
            {
                message.sendString(OptionsFull);
            }
            else
            {
                message.sendString(OptionsMin);
            }

            // 0.3. Send a request to the client asking to choose a menu option
            int max = loggedIn ? 6 : 2;
            int menuOption = message.requestNumberRange(1, max, "1-" + max + "> ");

            handleMenu(menuOption);
        }
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
        String appName = message.requestString("AppName: ");// 3.1. AppName

        String dateText = message.requestString("DateTime(e.g. 2007-12-27T10:15:30): ");// 3.2. DateTime
        LocalDateTime dateTime = LocalDateTime.parse(dateText);

        Platform platform = Platform.values()[message.requestNumberRange(0, 2, "Platform (0=Windows, 1=Mac, 2=Unix): ")];// 3.3. Platform

        String description = message.requestString("Description: ");// 3.4. Description

        Status status = Status.values()[message.requestNumberRange(0, 2, "Status (0=Open, 1=Closed, 2=Assigned): ")]; // 3.5. Status

        database.addBug(appName, dateTime, platform, description, status);
    }

    private void login()
    {
        Boolean validLogin = false;

        do
        {
            // 1.1. Send text to login
            message.sendString("Enter your email to login");

            // 1.2. Send request for email Prompt
            var email = message.requestString("Email: ");

            // 1.3. Send request for email Prompt
            var id = message.requestNumberRange(0, Integer.MAX_VALUE, "ID: ");

            for (User user : database.getUsers())
            {
                if (email.equalsIgnoreCase(user.Email()) && id == user.Id())
                {
                    name = " " + user.Name();
                    validLogin = true;
                    loggedIn = true;
                    showWelcomeMessage = true;
                }
            }

            // 1.4. Send error if client sent an invalid input
            message.sendBoolean(validLogin);

        } while (!validLogin);
    }

    private void register()
    {
        String name = message.requestString("Name: ");// 2.1. Name
        int id = message.requestNumber("ID: ");// 2.1. ID
        String email = message.requestString("Email: ");// 2.1. Email
        String department = message.requestString("Department: ");// 2.1. Department

        Boolean error = database.addUser(name, id, email, department);

        if (error)
        {
            message.sendString("Error adding new user to database!");
        }
        else
        {
            message.sendString("");
        }
    }
}
