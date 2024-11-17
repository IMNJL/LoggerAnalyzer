package backend.academy.logs.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LogReaderTest {

    private LogParser mockParser;
    private LogReader logReader;

    @BeforeEach
    void setUp() {
        mockParser = mock(LogParser.class);
        logReader = new LogReader(mockParser);
    }

    @Test
    void testReadLogsFromFile() throws IOException {
        // Arrange
        String logLine = "192.168.0.1 - - [16/Nov/2024:12:34:56 +0000] \"GET /index.html HTTP/1.1\" 200 1024 \"http://example.com\" \"Mozilla/5.0\"";
        Path tempFile = Files.createTempFile("test-log", ".log");
        Files.writeString(tempFile, logLine);

        LogEntry mockEntry = new LogEntry("192.168.0.1", "16/Nov/2024:12:34:56 +0000", "GET /index.html HTTP/1.1", 200, 1024, "http://example.com", "Mozilla/5.0");
        when(mockParser.parse(logLine)).thenReturn(mockEntry);

        // Act
        List<LogEntry> logs = logReader.readLogs(tempFile.toString());

        // Assert
        assertEquals(1, logs.size());
        assertEquals(mockEntry, logs.get(0));
        assertEquals(List.of(tempFile.getFileName().toString()), logReader.processedFiles());

        // Clean up
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testReadLogsFromDirectory() throws IOException {
        // Arrange
        String logLine1 = "192.168.0.1 - - [16/Nov/2024:12:34:56 +0000] \"GET /index.html HTTP/1.1\" 200 1024 \"http://example.com\" \"Mozilla/5.0\"";
        String logLine2 = "192.168.0.2 - - [16/Nov/2024:12:35:56 +0000] \"POST /login HTTP/1.1\" 401 512 \"http://example.com\" \"Mozilla/5.0\"";

        Path tempDir = Files.createTempDirectory("test-logs");
        Path logFile1 = Files.createFile(tempDir.resolve("log1.log"));
        Path logFile2 = Files.createFile(tempDir.resolve("log2.log"));

        Files.writeString(logFile1, logLine1);
        Files.writeString(logFile2, logLine2);

        LogEntry mockEntry1 = new LogEntry("192.168.0.1", "16/Nov/2024:12:34:56 +0000", "GET /index.html HTTP/1.1", 200, 1024, "http://example.com", "Mozilla/5.0");
        LogEntry mockEntry2 = new LogEntry("192.168.0.2", "16/Nov/2024:12:35:56 +0000", "POST /login HTTP/1.1", 401, 512, "http://example.com", "Mozilla/5.0");

        when(mockParser.parse(logLine1)).thenReturn(mockEntry1);
        when(mockParser.parse(logLine2)).thenReturn(mockEntry2);

        // Act
        List<LogEntry> logs = logReader.readLogs(tempDir.toString());

        // Assert
        assertEquals(2, logs.size());
        assertTrue(logs.contains(mockEntry1));
        assertTrue(logs.contains(mockEntry2));
        assertEquals(List.of(logFile2.getFileName().toString(), logFile1.getFileName().toString()), logReader.processedFiles());

        // Clean up
        Files.deleteIfExists(logFile1);
        Files.deleteIfExists(logFile2);
        Files.deleteIfExists(tempDir);
    }

    @Test
    void testReadLogsFromInvalidFile() {
        // Arrange
        String invalidPath = "invalid/path/to/logfile.log";

        // Act & Assert
        IOException exception = assertThrows(IOException.class, () -> logReader.readLogs(invalidPath));
        assertTrue(exception.getMessage().contains(invalidPath), "Exception message should reference the invalid path");
    }


    @Test
    void testReadLogsWithUnparsableLine() throws IOException {
        // Arrange
        String logLine = "INVALID LOG LINE";
        Path tempFile = Files.createTempFile("test-log", ".log");
        Files.writeString(tempFile, logLine);

        when(mockParser.parse(logLine)).thenReturn(null);

        // Act
        List<LogEntry> logs = logReader.readLogs(tempFile.toString());

        // Assert
        assertTrue(logs.isEmpty());
        assertEquals(List.of(tempFile.getFileName().toString()), logReader.processedFiles());

        // Clean up
        Files.deleteIfExists(tempFile);
    }
}
