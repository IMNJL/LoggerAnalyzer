package backend.academy.logs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {
    private static final Pattern LOG_PATTERN = Pattern.compile(
        "(?<ip>\\S+) \\S+ \\S+ \\[(?<timestamp>[^\\]]+)] \"(?<request>[^\"]+)\" (?<status>\\d{3}) (?<size>\\d*) \"(?<referer>-|[^\"]*)\" \"(?<userAgent>[^\"]*)\""
    );

    public static LogEntry parseLogLine(String line) {
        Matcher matcher = LOG_PATTERN.matcher(line);
        if (matcher.matches()) {
            return new LogEntry(
                matcher.group("ip"),
                matcher.group("timestamp"),
                matcher.group("request"),
                Integer.parseInt(matcher.group("status")),
                Integer.parseInt(matcher.group("size")),
                matcher.group("referer"),
                matcher.group("userAgent")
            );
        }
        return null;
    }
}
