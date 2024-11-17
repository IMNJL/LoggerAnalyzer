package backend.academy.logs.utils;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class HttpFileDownloaderTest {

    @Test
    void testDownloadAsStream_Failure_InvalidDomain() {
        // Arrange
        String invalidUrl = "https://example.com/somefile.txt";

        // Act & Assert
        IOException thrown = assertThrows(IOException.class, () -> HttpFileDownloader.downloadAsStream(invalidUrl));
        assertTrue(thrown.getMessage().contains("Access to this domain is not allowed"));
    }

    @Test
    void testDownloadAsStream_Failure_InvalidProtocol() {
        // Arrange
        String invalidUrl = "ftp://raw.githubusercontent.com/somefile.txt";

        // Act & Assert
        IOException thrown = assertThrows(IOException.class, () -> HttpFileDownloader.downloadAsStream(invalidUrl));
        assertTrue(thrown.getMessage().contains("Invalid protocol"));
    }

    @Test
    void testDownloadAsStream_Failure_HTTPError() throws IOException {
        // Arrange
        String validUrl = "https://raw.githubusercontent.com/somefile.txt";
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND); // 404 error
        when(mockConnection.getURL()).thenReturn(new URL(validUrl));

        // Act & Assert
        IOException thrown = assertThrows(IOException.class, () -> HttpFileDownloader.downloadAsStream(validUrl));
        assertTrue(thrown.getMessage().contains("Failed to download file"));
    }

    @Test
    void testGetHttpURLConnection_InvalidDomain() throws IOException {
        // Arrange
        String invalidUrl = "https://example.com";

        // Act & Assert
        IOException thrown = assertThrows(IOException.class, () -> HttpFileDownloader.getHttpURLConnection(invalidUrl));
        assertTrue(thrown.getMessage().contains("Access to this domain is not allowed"));
    }

    @Test
    void testGetHttpURLConnection_InvalidProtocol() throws IOException {
        // Arrange
        String invalidUrl = "ftp://raw.githubusercontent.com/somefile.txt";

        // Act & Assert
        IOException thrown = assertThrows(IOException.class, () -> HttpFileDownloader.getHttpURLConnection(invalidUrl));
        assertTrue(thrown.getMessage().contains("Invalid protocol"));
    }
}
