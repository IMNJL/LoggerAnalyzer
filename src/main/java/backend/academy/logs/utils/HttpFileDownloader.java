package backend.academy.logs.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HttpFileDownloader {
    private static final int TIMEOUT = 5000;
    /**
     * Список разрешённых доменов + дополняется
     */
    private static final List<String> ALLOWED_DOMAINS = Collections.singletonList(
        "raw.githubusercontent.com"
    );

    public static InputStream downloadAsStream(String urlString) throws IOException {
        HttpURLConnection connection = getHttpURLConnection(urlString);

        // Проверяем код ответа
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to download file. HTTP response code: " + responseCode);
        }

        return connection.getInputStream();
    }

    public static HttpURLConnection getHttpURLConnection(String urlString) throws IOException {
        URL url = new URL(urlString);

        // Проверка протокола
        if (!"http".equalsIgnoreCase(url.getProtocol()) && !"https".equalsIgnoreCase(url.getProtocol())) {
            throw new IOException("Invalid protocol: " + url.getProtocol());
        }

        // Проверка на разрешённые домены
        if (!isAllowedDomain(url)) {
            throw new IOException("Access to this domain is not allowed: " + url.getHost());
        }

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(TIMEOUT); // Таймаут подключения 5 секунд
        connection.setReadTimeout(TIMEOUT);
        return connection;
    }

    /**
     * Проверяет, принадлежит ли домен списку разрешённых.
     */
    private static boolean isAllowedDomain(URL url) throws IOException {
        String host = url.getHost();

        // Проверяем, является ли хост разрешённым
        boolean isAllowed = ALLOWED_DOMAINS.stream().anyMatch(host::endsWith);
        if (!isAllowed) {
            return false;
        }

        // Резолвим IP-адрес и проверяем его
        InetAddress address = InetAddress.getByName(host);
        if (address.isLoopbackAddress() || address.isAnyLocalAddress() || address.isSiteLocalAddress()) {
            throw new IOException("Access to private or local network addresses "
                + "is not allowed: " + address.getHostAddress());
        }

        return true;
    }
}
