import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Map;

import Database.Bug;
import Database.Database;
import Database.Platform;
import Database.Status;
import Database.User;

/*
 * A Network message sent from client to server or server to client
*/
public class ServerLogic extends Thread
{
    private MessageHandler message;

    private final String OptionsMin = "1. Register\n2. Login\n";
    private final String OptionsFull = OptionsMin + "3. Add Bug\n4. Assign Bug\n5. View all Bugs Not Assigned\n6. Update bug\n";
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
            int menuOption = message.requestNumber("1-" + max + ": ", 1, max);

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
    }

    private void viewAllBugs()
    {
        var bugs = database.getBugs();

        StringBuilder allBugs = new StringBuilder();

        for (var bug : bugs.entrySet())
        {
            var data = bug.getValue();

            // Only display the none assigned bugs
            if (data.userID() != 0)
            {
                continue;
            }

            allBugs.append("Bug (" + bug.getKey() + ")\n");
            allBugs.append("AppName:\t" + data.appName() + "\n");
            allBugs.append("DateTime:\t" + data.dateTime() + "\n");
            allBugs.append("Platform:\t" + data.platform() + "\n");
            allBugs.append("Description:\t" + data.description() + "\n");
            allBugs.append("Status:\t" + data.status() + "\n");
            allBugs.append("\n");
        }

        message.sendString(allBugs.toString());
    }

    private void assignBug()
    {
        int bugID = 0;
        Boolean inputInvalid = true;
        while (inputInvalid)
        {
            bugID = message.requestNumber("BugID to assign to the user: "); // 4.1. BugID

            for (var id : database.getBugs().keySet())
            {
                if (id == bugID)
                {
                    inputInvalid = false;
                }
            }

            message.sendBoolean(inputInvalid);
        }

        int userID = 0;
        inputInvalid = true;
        while (inputInvalid)
        {
            userID = message.requestNumber("UserID to assign bug to: ");// 4.2. UserID to assign

            for (var user : database.getUsers())
            {
                if (user.id() == userID)
                {
                    inputInvalid = false;
                }
            }

            message.sendBoolean(inputInvalid);
        }

        Map<Integer, Bug> bugs = database.getBugs();

        Bug oldBug = bugs.get(bugID);

        Bug updatedBug = new Bug(oldBug.appName(), oldBug.dateTime(), oldBug.platform(), oldBug.description(), oldBug.status(), userID);
        bugs.put(bugID, updatedBug);
    }

    private void addBug()
    {
        String appName = message.requestString("AppName: "); // 3.1. AppName
        LocalDateTime dateTime = message.requestDate("DateTime (e.g. 2007-12-27T10:15:30): "); // 3.2. DateTime
        int platformIndex = message.requestNumber("Platform (0=Windows, 1=Mac, 2=Unix): ", 0, 2); // 3.3. Platform
        String description = message.requestString("Description: "); // 3.4. Description
        int statusIndex = message.requestNumber("Status (0=Open, 1=Closed, 2=Assigned): ", 0, 2); // 3.5. Status

        Platform platform = Platform.values()[platformIndex];
        Status status = Status.values()[statusIndex];

        database.addBug(appName, dateTime, platform, description, status);
    }

    private void login()
    {
        Boolean validLogin = false;

        // 2.1. Send text to login
        message.sendString("Enter your email to login");

        // 2.2. Send request for email Prompt
        var email = message.requestString("Email: ");

        // 2.3. Send request for email Prompt
        var id = message.requestNumber("ID: ", 0, Integer.MAX_VALUE);

        for (User user : database.getUsers())
        {
            if (email.equalsIgnoreCase(user.email()) && id == user.id())
            {
                name = " " + user.name();
                validLogin = true;
                loggedIn = true;
                showWelcomeMessage = true;
            }
        }

        // 2.4. Send error if client sent an invalid input
        message.sendBoolean(validLogin);
    }

    private void register()
    {
        String name = message.requestString("Name: ");// 1.1. Name
        int id = message.requestNumber("ID: ", 1, Integer.MAX_VALUE);// 1.2. ID
        String email = message.requestString("Email: ");// 1.3. Email
        String department = message.requestString("Department: ");// 1.4. Department

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
