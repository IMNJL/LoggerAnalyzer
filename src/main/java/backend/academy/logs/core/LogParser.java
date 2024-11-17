package backend.academy.logs.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {
    private static final int IP = 1;
    private static final int TIMESTAMP = 2;
    private static final int REQUEST = 3;
    private static final int STATUS = 4;
    private static final int SIZE = 5;
    private static final int REFERER = 6;
    private static final int USERAGENT = 6;
    private static final Pattern LOG_PATTERN = Pattern.compile(
        "^(\\S+) \\S+ \\S+ \\[(.+?)] \"(.+?)\" (\\d{3}) (\\d+) \"(.*?)\" \"(.*?)\"$"
    );

    public LogEntry parse(String logLine) {
        Matcher matcher = LOG_PATTERN.matcher(logLine);
        if (!matcher.matches()) {
            return null;
        }

        return new LogEntry(
            matcher.group(IP),
            matcher.group(TIMESTAMP),
            matcher.group(REQUEST),
            Integer.parseInt(matcher.group(STATUS)),
            Integer.parseInt(matcher.group(SIZE)),
            matcher.group(REFERER),
            matcher.group(USERAGENT)
        );
    }
}
