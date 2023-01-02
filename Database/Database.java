package Database;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Database
{
    private static final String BugsHeader = "--- Bugs ---";

    private Collection<User> users = new LinkedList<>();
    private Map<Integer, Bug> bugs = new HashMap<Integer, Bug>();

    int nextBugID = 1;

    public Database()
    {
    }

    /**
     * @return all the users
     */
    public synchronized Collection<User> getUsers()
    {
        return users;
    }

    /**
     * @return all the bugs
     */
    public synchronized Map<Integer, Bug> getBugs()
    {
        return bugs;
    }

    /**
     * Add a new user to the database
     *
     * @param name       New Clients name
     * @param id         New Clients id
     * @param email      New Clients email
     * @param department New Clients department
     * @return False if it had an error adding the user
     */
    public synchronized Boolean addUser(String name, int id, String email, String department)
    {
        User newUser = new User(name, id, email, department);

        for (User user : users)
        {
            // Emails are case insensitive
            if (newUser.email().equalsIgnoreCase(user.email()))
            {
                return true;
            }

            // Check if the email has an @ and .
            if (newUser.email().indexOf("@") == -1 && newUser.email().indexOf(".") == -1)
            {
                return true;
            }

            if (newUser.id() == user.id())
            {
                return true;
            }
        }

        System.out.println("Added new user (name:" + name + ", id:" + id + ", email:" + email + ", department:" + department + ")");
        users.add(newUser);
        return false;
    }

    public synchronized void addBug(String appName, LocalDateTime dateTime, Platform platform, String description, Status status)
    {
        Bug newBug = new Bug(appName, dateTime, platform, description, status, 0);

        System.out.println("Added new bug (AppName:" + appName + ", DateTime:" + dateTime + ", Platform:" + platform + ", department:" + description + ", Status:" + status + ")");

        // Make the random number be negative to avoid overlap with the users ids
        bugs.put(nextBugID, newBug);
        nextBugID++;
    }

    public void load()
    {
        List<String> lines;

        try
        {
            lines = Files.readAllLines(Path.of("./Database.csv"));
        }
        catch (IOException e)
        {
            return;
        }

        var linesIter = lines.iterator();

        nextBugID = Integer.parseInt(linesIter.next());

        Boolean readingBugs = false;
        while (linesIter.hasNext())
        {
            String line = linesIter.next();
            if (line.equalsIgnoreCase(BugsHeader))
            {
                readingBugs = true;
                continue;
            }

            String[] data = line.split(",");

            if (readingBugs)
            {
                var newBug = new Bug(data[1], LocalDateTime.parse(data[2]), Platform.valueOf(data[3]), data[4], Status.valueOf(data[5]), Integer.parseInt(data[6]));
                bugs.put(Integer.parseInt(data[0]), newBug);
            }
            else
            {
                var newUser = new User(data[0], Integer.parseInt(data[1]), data[2], data[3]);
                users.add(newUser);
            }
        }
    }

    public synchronized void save()
    {
        try (var file = new FileWriter(new File("./Database.csv")))
        {
            file.append("" + nextBugID + "\n");

            for (User user : users)
            {
                file.append(user.name() + "," + user.id() + "," + user.email() + "," + user.department() + "\n");
            }

            // Seperator between the data
            file.append(BugsHeader + "\n");

            for (var data : bugs.entrySet())
            {
                var id = data.getKey();
                var bug = data.getValue();
                file.append(id + "," + bug.appName() + "," + bug.dateTime() + "," + bug.platform() + "," + bug.description() + "," + bug.status() + "," + bug.userID() + "\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
