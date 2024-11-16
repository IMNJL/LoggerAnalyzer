package backend.academy.logs.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpFileDownloader {

    public static File download(String urlString, String outputDir) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Проверяем код ответа
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to download file. HTTP response code: " + responseCode);
        }

        // Определяем имя файла из URL
        String fileName = new File(url.getPath()).getName();
        if (fileName.isEmpty()) {
            throw new IOException("Failed to determine file name from URL: " + urlString);
        }

        // Создаем директорию, если ее нет
        File directory = new File(outputDir);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Failed to create output directory: " + outputDir);
        }

        // Путь для сохранения файла
        File outputFile = new File(directory, fileName);

        // Скачиваем файл
        try (InputStream inputStream = connection.getInputStream();
             FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        }

        return outputFile;
    }
}
