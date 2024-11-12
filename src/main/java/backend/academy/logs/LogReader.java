package backend.academy.logs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogReader {
    private final LogParser logParser;

    public LogReader(LogParser logParser) {
        this.logParser = logParser;
    }

    public List<LogEntry> readLogFile(String filePath) throws IOException {
        List<LogEntry> logs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                LogEntry logEntry = logParser.parseLogLine(line);
                if (logEntry != null) {
                    logs.add(logEntry);
                }
            }
        }
        return logs;
    }
}

