
public class User
{
    private String name;
    private int id;
    private String email;
    private String department;

    public User(String name, int id, String email, String department)
    {
        this.name = name;
        this.id = id;
        this.email = email;
        this.department = department;
    }

    public String getName()
    {
        return name;
    }

    public int getId()
    {
        return id;
    }

    public String getEmail()
    {
        return email;
    }

    public String getDepartment()
    {
        return department;
    }
}
