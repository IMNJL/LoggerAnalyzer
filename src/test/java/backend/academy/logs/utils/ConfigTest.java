package backend.academy.logs.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigTest {

    @Test
    void testGetStatusName() {
        // Informational Response (1xx)
        assertEquals("Informational Response", Config.getStatusName(100), "Status 100 should be Informational Response");
        assertEquals("Informational Response", Config.getStatusName(199), "Status 199 should be Informational Response");

        // OK (2xx)
        assertEquals("OK", Config.getStatusName(200), "Status 200 should be OK");
        assertEquals("OK", Config.getStatusName(299), "Status 299 should be OK");

        // Redirection (3xx)
        assertEquals("Redirection", Config.getStatusName(300), "Status 300 should be Redirection");
        assertEquals("Redirection", Config.getStatusName(399), "Status 399 should be Redirection");

        // Client Error (4xx)
        assertEquals("Client Error", Config.getStatusName(400), "Status 400 should be Client Error");
        assertEquals("Client Error", Config.getStatusName(499), "Status 499 should be Client Error");

        // Internal Server Error (5xx)
        assertEquals("Internal Server Error", Config.getStatusName(500), "Status 500 should be Internal Server Error");
        assertEquals("Internal Server Error", Config.getStatusName(599), "Status 599 should be Internal Server Error");

        // Unknown (other codes)
        assertEquals("Unknown", Config.getStatusName(600), "Status 600 should be Unknown");
        assertEquals("Unknown", Config.getStatusName(99), "Status 99 should be Unknown");
    }

    @Test
    void testConfigGettersAndSetters() {
        Config config = new Config();

        // Setting values
        config.path("path/to/logs");
        config.format("markdown");
        config.from("2024-01-01");
        config.to("2024-12-31");

        // Verifying values
        assertEquals("path/to/logs", config.path(), "Path should match the set value");
        assertEquals("markdown", config.format(), "Format should match the set value");
        assertEquals("2024-01-01", config.from(), "From date should match the set value");
        assertEquals("2024-12-31", config.to(), "To date should match the set value");
    }
}
