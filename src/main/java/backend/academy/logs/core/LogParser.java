package backend.academy.logs.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {
    private static final Pattern LOG_PATTERN = Pattern.compile(
        "^(\\S+) \\S+ \\S+ \\[(.+?)] \"(.+?)\" (\\d{3}) (\\d+) \"(.*?)\" \"(.*?)\"$"
    );

    public LogEntry parse(String logLine) {
        Matcher matcher = LOG_PATTERN.matcher(logLine);
        if (!matcher.matches()) return null;

        return new LogEntry(
            matcher.group(1),
            matcher.group(2),
            matcher.group(3),
            Integer.parseInt(matcher.group(4)),
            Integer.parseInt(matcher.group(5)),
            matcher.group(6),
            matcher.group(7)
        );
    }
}
