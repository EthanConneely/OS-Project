package Database;

import java.time.LocalDateTime;

public record Bug(String appName, LocalDateTime dateTime, Platform platform, String description, Status status, Integer userID)
{
}
