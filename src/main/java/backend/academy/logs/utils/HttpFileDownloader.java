package backend.academy.logs.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpFileDownloader {
        public static InputStream downloadAsStream(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Проверяем код ответа
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to download file. HTTP response code: " + responseCode);
        }

        return connection.getInputStream();
    }
}
