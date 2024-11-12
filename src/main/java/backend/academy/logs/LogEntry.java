package backend.academy.logs;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter public class LogEntry {
    private String ip;
    private String timestamp;
    private String request;
    private int status;
    private int size;
    private String referer;
    private String userAgent;

    public LogEntry(String ip, String timestamp, String request, int status, int size, String referer, String userAgent) {
        this.ip = ip;
        this.timestamp = timestamp;
        this.request = request;
        this.status = status;
        this.size = size;
        this.referer = referer;
        this.userAgent = userAgent;
    }
}

