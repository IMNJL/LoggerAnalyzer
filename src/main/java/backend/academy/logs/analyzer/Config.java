package backend.academy.logs.analyzer;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Config {
    @Getter private static final String outputDir = "src/main/resources/log_files";
    @Getter private static final String markdownPath = "src/main/resources/reports/report.md";
    @Getter private static final String asciidocPath = "src/main/resources/reports/report.adoc";
    @Getter String path;
    @Getter String format;
    @Getter String from;
    @Getter String to;

    // Метод для получения имени кода ответа
    public static String getStatusName(int statusCode) {
        return switch (statusCode / 100) {
            case 1 -> "Informational Response";
            case 2 -> "OK";
            case 3 -> "Redirection";
            case 4 -> "Client Error";
            case 5 -> "Internal Server Error";
            default -> "Unknown";
        };
    }
}
