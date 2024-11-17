package backend.academy.logs.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogParserTest {

    private final LogParser parser = new LogParser();

    @Test
    void testParse_ValidLogLine() {
        String logLine = "192.168.1.1 - - [10/Nov/2024:14:32:01 +0000] \"GET /index.html HTTP/1.1\" 200 512 \"http://example.com\" \"Mozilla/5.0\"";

        LogEntry entry = parser.parse(logLine);

        assertNotNull(entry, "LogEntry should not be null for a valid log line");
        assertEquals("192.168.1.1", entry.ip(), "IP should match");
        assertEquals("10/Nov/2024:14:32:01 +0000", entry.timestamp(), "Timestamp should match");
        assertEquals("GET /index.html HTTP/1.1", entry.request(), "Request should match");
        assertEquals(200, entry.status(), "Status code should match");
//        assertEquals(512, entry.responseSize(), "Response size should match");
        assertEquals("http://example.com", entry.referer(), "Referer should match");
        assertEquals("Mozilla/5.0", entry.userAgent(), "User-Agent should match");
    }

    @Test
    void testParse_InvalidLogLine() {
        String logLine = "INVALID LOG LINE";

        LogEntry entry = parser.parse(logLine);

        assertNull(entry, "LogEntry should be null for an invalid log line");
    }

    @Test
    void testParse_BoundaryCase_MinimumFields() {
        String logLine = "10.0.0.1 - - [01/Jan/2024:00:00:00 +0000] \"HEAD /empty HTTP/1.0\" 204 0 \"-\" \"-\"";

        LogEntry entry = parser.parse(logLine);

        assertNotNull(entry, "LogEntry should not be null for a minimal valid log line");
        assertEquals("10.0.0.1", entry.ip(), "IP should match");
        assertEquals("01/Jan/2024:00:00:00 +0000", entry.timestamp(), "Timestamp should match");
        assertEquals("HEAD /empty HTTP/1.0", entry.request(), "Request should match");
        assertEquals(204, entry.status(), "Status code should match");
//        assertEquals(0, entry.responseSize(), "Response size should match");
        assertEquals("-", entry.referer(), "Referer should match");
        assertEquals("-", entry.userAgent(), "User-Agent should match");
    }

    @Test
    void testParse_MalformedLogLine() {
        String logLine = "192.168.1.1 - - [10/Nov/2024:14:32:01 +0000]";

        LogEntry entry = parser.parse(logLine);

        assertNull(entry, "LogEntry should be null for a malformed log line");
    }
}
