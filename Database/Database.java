package Database;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class Database
{
    private Collection<User> users = new LinkedList<>();
    private Map<UUID, Bug> bugs = new HashMap<UUID, Bug>();

    public Database()
    {
        // For quick debugging
        users.add(new User("1", 1, "1", "1"));
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
    public synchronized Map<UUID, Bug> getBugs()
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
            if (newUser.Email().equalsIgnoreCase(user.Email()))
            {
                return true;
            }

            // Check if the email has a @ and .
            if (newUser.Email().indexOf("@") == -1 && newUser.Email().indexOf(".") == -1)
            {
                return true;
            }

            if (newUser.Id() == user.Id())
            {
                return true;
            }
        }

        System.out.println("Added new user (name:" + name + ", id:" + id + ", email:" + email + ", department:" + department + ")");
        users.add(newUser);
        return false;
    }

    public synchronized void addBug(String AppName, LocalDateTime DateTime, Platform Platform, String Description, Status Status)
    {
        Bug newBug = new Bug(AppName, DateTime, Platform, Description, Status);

        bugs.put(UUID.randomUUID(), newBug);
    }
}
