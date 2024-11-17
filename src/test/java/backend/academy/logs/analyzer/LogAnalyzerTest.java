package backend.academy.logs.analyzer;

import backend.academy.logs.core.LogEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LogAnalyzerTest {
    private LogAnalyzer logAnalyzer;

    @BeforeEach
    void setUp() {
        List<LogEntry> logs = List.of(
            new LogEntry("192.168.0.1", "2024-11-15T10:00:00", "GET /index.html", 200, 500, "http://example.com", "Mozilla/5.0"),
            new LogEntry("192.168.0.2", "2024-11-15T10:05:00", "GET /about.html", 404, 300, "http://example.com", "Mozilla/5.0"),
            new LogEntry("192.168.0.1", "2024-11-15T10:10:00", "GET /contact.html", 200, 700, "http://example.com", "Mozilla/5.0"),
            new LogEntry("192.168.0.3", "2024-11-15T10:15:00", "GET /index.html", 500, 200, "http://example.com", "Mozilla/5.0"),
            new LogEntry("192.168.0.4", "2024-11-15T10:20:00", "GET /index.html", 200, 1000, "http://example.com", "Mozilla/5.0")
        );

        logAnalyzer = new LogAnalyzer(logs);
    }

    @Test
    void testGetTotalRequests() {
        assertEquals(5, logAnalyzer.getTotalRequests(), "Total requests should match the number of log entries.");
    }

    @Test
    void testGetAverageResponseSize() {
        assertEquals(540.0, logAnalyzer.getAverageResponseSize(), 0.01, "Average response size should be correctly calculated.");
    }

    @Test
    void testGetResponseSizePercentile() {
        assertEquals(500, logAnalyzer.getResponseSizePercentile(50), "50th percentile response size should be correct.");
        assertEquals(1000, logAnalyzer.getResponseSizePercentile(100), "100th percentile response size should be correct.");
        assertEquals(Double.NaN, logAnalyzer.getResponseSizePercentile(110), "Invalid percentile should return NaN.");
    }

    @Test
    void testGetMostFrequentResources() {
        Map<String, Long> mostFrequentResources = logAnalyzer.getMostFrequentResources();
        assertEquals(1, mostFrequentResources.get("GET /about.html"), "Resource frequency should be correct.");
        assertEquals(3, mostFrequentResources.get("GET /index.html"), "Resource frequency should be correct.");
    }

    @Test
    void testGetResponseCodeCounts() {
        Map<Integer, Long> responseCodeCounts = logAnalyzer.getResponseCodeCounts();
        assertEquals(3, responseCodeCounts.get(200), "Response code counts for 200 should match.");
        assertEquals(1, responseCodeCounts.get(404), "Response code counts for 404 should match.");
        assertEquals(1, responseCodeCounts.get(500), "Response code counts for 500 should match.");
    }

    @Test
    void testGetMaxResponseSize() {
        assertEquals(1000, logAnalyzer.getMaxResponseSize(), "Maximum response size should be correct.");
    }

    @Test
    void testGetUniqueIpCount() {
        assertEquals(4, logAnalyzer.getUniqueIpCount(), "Unique IP count should be correct.");
    }

    @Test
    void testFilterLogs() {
        List<LogEntry> filteredLogs = logAnalyzer.filterLogs(log -> log.status() == 200);
        assertEquals(3, filteredLogs.size(), "Filtered logs for status 200 should be correct.");
        assertTrue(filteredLogs.stream().allMatch(log -> log.status() == 200), "All filtered logs should have status 200.");
    }
}
