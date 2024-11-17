package backend.academy.logs.core;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class LogEntry {
    private final String ip;
    private final String timestamp;
    private final String request;
    private final int status;
    private final int size;
    private final String referer;
    private final String userAgent;

    public LogEntry(String ip, String timestamp,
        String request, int status,
        int size, String referer, String userAgent) {
        this.ip = ip;
        this.timestamp = timestamp;
        this.request = request;
        this.status = status;
        this.size = size;
        this.referer = referer;
        this.userAgent = userAgent;
    }
}
