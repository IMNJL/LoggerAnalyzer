package backend.academy.logs.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpFileDownloader {
    public static BufferedReader download(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream inputStream = connection.getInputStream();
        return new BufferedReader(new InputStreamReader(inputStream));
    }
}

