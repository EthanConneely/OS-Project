package Database;

import java.time.LocalDateTime;

public record Bug(String AppName, LocalDateTime DateTime, Platform Platform, String Description, Status Status)
{
}
