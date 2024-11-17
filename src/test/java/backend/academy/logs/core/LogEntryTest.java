package backend.academy.logs.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogEntryTest {

    @Test
    void testConstructorAndGetters() {
        LogEntry logEntry = new LogEntry(
            "192.168.0.1",
            "2024-11-16T12:34:56Z",
            "GET /index.html HTTP/1.1",
            200,
            1024,
            "http://example.com",
            "Mozilla/5.0"
        );

        assertEquals("192.168.0.1", logEntry.ip());
        assertEquals("2024-11-16T12:34:56Z", logEntry.timestamp());
        assertEquals("GET /index.html HTTP/1.1", logEntry.request());
        assertEquals(200, logEntry.status());
        assertEquals(1024, logEntry.size());
        assertEquals("http://example.com", logEntry.referer());
        assertEquals("Mozilla/5.0", logEntry.userAgent());
    }

    @Test
    void testSettersForMutableFields() {
        LogEntry logEntry = new LogEntry(
            "192.168.0.1",
            "2024-11-16T12:34:56Z",
            "GET /index.html HTTP/1.1",
            200,
            1024,
            "http://example.com",
            "Mozilla/5.0"
        );

        // Test setting mutable fields
        logEntry.referer("http://updated-example.com");
        logEntry.userAgent("UpdatedUserAgent/1.0");

        assertEquals("http://updated-example.com", logEntry.referer());
        assertEquals("UpdatedUserAgent/1.0", logEntry.userAgent());
    }

    @Test
    void testImmutabilityOfFinalFields() {
        LogEntry logEntry = new LogEntry(
            "192.168.0.1",
            "2024-11-16T12:34:56Z",
            "GET /index.html HTTP/1.1",
            200,
            1024,
            "http://example.com",
            "Mozilla/5.0"
        );

        // Fields marked as final should not have setters
        assertThrows(NoSuchMethodException.class,
            () -> LogEntry.class.getMethod("setIp", String.class));
        assertThrows(NoSuchMethodException.class,
            () -> LogEntry.class.getMethod("setTimestamp", String.class));
        assertThrows(NoSuchMethodException.class,
            () -> LogEntry.class.getMethod("setRequest", String.class));
        assertThrows(NoSuchMethodException.class,
            () -> LogEntry.class.getMethod("setStatus", int.class));
        assertThrows(NoSuchMethodException.class,
            () -> LogEntry.class.getMethod("setSize", int.class));
    }

    @Test
    void testEquality() {
        LogEntry logEntry1 = new LogEntry(
            "192.168.0.1",
            "2024-11-16T12:34:56Z",
            "GET /index.html HTTP/1.1",
            200,
            1024,
            "http://example.com",
            "Mozilla/5.0"
        );

        LogEntry logEntry2 = new LogEntry(
            "192.168.0.1",
            "2024-11-16T12:34:56Z",
            "GET /index.html HTTP/1.1",
            200,
            1024,
            "http://example.com",
            "Mozilla/5.0"
        );

        assertNotEquals(logEntry1, logEntry2);
        assertNotEquals(logEntry1.hashCode(), logEntry2.hashCode());
    }
}

