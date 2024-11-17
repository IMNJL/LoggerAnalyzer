package backend.academy.logs.utils;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Config {
    private static final int HUNDRED = 100;
    private static final int INFORMATIONAL_RESPONSE = 1;
    private static final int OK = 2;
    private static final int REDIRECTION = 3;
    private static final int CLIENT_ERROR = 4;
    private static final int INTERNAL_SERVER_ERROR = 5;
    @Getter String path;
    @Getter String format;
    @Getter String from;
    @Getter String to;

    // Метод для получения имени кода ответа
    public static String getStatusName(int statusCode) {
        return switch (statusCode / HUNDRED) {
            case INFORMATIONAL_RESPONSE -> "Informational Response";
            case OK -> "OK";
            case REDIRECTION -> "Redirection";
            case CLIENT_ERROR -> "Client Error";
            case INTERNAL_SERVER_ERROR -> "Internal Server Error";
            default -> "Unknown";
        };
    }
}
