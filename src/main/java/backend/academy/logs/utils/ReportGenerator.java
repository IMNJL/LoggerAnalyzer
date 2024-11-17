package backend.academy.logs.utils;

import backend.academy.logs.analyzer.LogAnalyzer;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import static backend.academy.logs.utils.CommandLineArgs.get;
import static backend.academy.logs.utils.Config.getStatusName;

@UtilityClass
public class ReportGenerator {
    private static final int PERCENTILE = 95;

    @SuppressWarnings("VA_FORMAT_STRING_USES_NEWLINE")
    public static void generateMarkdownReport(LogAnalyzer analyzer, List<String> processedFiles) throws IOException {
        Path outputPath = Paths.get(CommandLineConfig.markdownPath()).normalize();

        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
            // Раздел: Общая информация
            writer.write("#### Общая информация\n\n");
            writer.write("| Метрика               | Значение           |\n");
            writer.write("|:----------------------|--------------------|\n");
            writer.write(String.format("| Файл(-ы)             | `%s`              |\n",
                String.join(", ", processedFiles)));
            writer.write(String.format("| Начальная дата        | %s                |\n", Optional
                .ofNullable(fromm()).orElse("Не укaзано")));
            writer.write(String.format("| Конечная дата         | %s                |\n", Optional
                .ofNullable(too()).orElse("Не указaно")));
            writer.write(String.format("| Количество запросов   | %,d               |\n", analyzer
                .getTotalRequests()));
            writer.write(String.format("| Средний размер ответа | %,12.0f байт      |\n", analyzer
                .getAverageResponseSize()));
            writer.write(String.format("| 95-й перцентиль       | %,12.0f байт      |\n\n", analyzer
                .getResponseSizePercentile(PERCENTILE)));

            // Раздел: Запрашиваемые ресурсы
            writer.write("#### Запрашиваемые ресурсы\n\n");
            writer.write("| Ресурс                         | Количество       |\n");
            writer.write("|:-------------------------------|------------------|\n");
            for (Map.Entry<String, Long> entry : analyzer.getMostFrequentResources().entrySet()) {
                writer.write(String.format("| %-30s | %,12d |\n", Optional
                    .ofNullable(entry.getKey()).orElse("Неизвeстный ресурс"), entry.getValue()));
            }

            // Раздел: Коды ответа
            writer.write("\n#### Коды ответа\n\n");
            writer.write("| Код  | Имя                 | Количество        |\n");
            writer.write("|:-----|:--------------------|-------------------|\n");
            for (Map.Entry<Integer, Long> entry : analyzer.getResponseCodeCounts().entrySet()) {
                writer.write(String.format("| %-5d | %-20s | %,12d |\n",
                    entry.getKey(),
                    Optional.ofNullable(getStatusName(entry.getKey())).orElse("Неизвестный статуc"),
                    entry.getValue()));
            }

            // Раздел доп баллы: доп функция
            writer.write("\n#### Доп. статистика\n\n");
            writer.write(String.format("| Максимальный размер ответа | %,12d байт      |\n", analyzer.getMaxResponseSize()));
            writer.write(String.format("| Уникальных IP-адресов      | %,d               |\n", analyzer.getUniqueIpCount()));
        }
    }

    public static void generateAsciiDocReport(LogAnalyzer analyzer, List<String> processedFiles) throws IOException {
        Path outputPath = Paths.get(CommandLineConfig.asciidocPath()).normalize();

        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
            // Раздел: Общая информация
            writer.write("== Общая информация\n\n");
            endBg(writer);
            writer.write("| Метрика               | Значение\n");
            writer.write(String.format("| Файл(-ы)             | `%s`\n", String.join(", ", processedFiles)));
            writer.write(String.format("| Начальная дата        | %s\n", Optional.ofNullable(fromm())
                .orElse("Не указанo")));
            writer.write(String.format("| Конечная дата         | %s\n", Optional.ofNullable(too())
                .orElse("Не указано")));
            writer.write(String.format("| Количество запросов   | %,d\n", analyzer.getTotalRequests()));
            writer.write(String.format("| Средний размер ответа | %,12.0f байт\n", analyzer.getAverageResponseSize()));
            writer.write(String.format("| 95-й перцентиль       | %,12.0f байт\n", analyzer
                .getResponseSizePercentile(PERCENTILE)));
            endline(writer);

            // Раздел: Запрашиваемые ресурсы
            writer.write("== Запрашиваемые ресурсы\n\n");
            endBg(writer);
            writer.write("| Ресурс                         | Количество\n");
            for (Map.Entry<String, Long> entry : analyzer.getMostFrequentResources().entrySet()) {
                writer.write(String.format("| %-30s | %,12d\n", Optional
                    .ofNullable(entry.getKey()).orElse("Неизвестный ресурс"), entry.getValue()));
            }
            endline(writer);

            // Раздел: Коды ответа
            writer.write("== Коды ответа\n\n");
            endBg(writer);
            writer.write("| Код  | Имя                 | Количество\n");
            for (Map.Entry<Integer, Long> entry : analyzer.getResponseCodeCounts().entrySet()) {
                writer.write(String.format("| %-5d | %-20s | %,12d\n",
                    entry.getKey(),
                    Optional.ofNullable(getStatusName(entry.getKey())).orElse("Неизвестный статус"),
                    entry.getValue()));
            }
            abzac(writer);

            // Раздел доп баллы: доп функция
            writer.write("== Доп. статистика\n\n");
            writer.write(String.format("| Максимальный размер ответа | %,12d байт\n", analyzer.getMaxResponseSize()));
            writer.write(String.format("| Уникальных IP-адресов      | %,d\n", analyzer.getUniqueIpCount()));
        }
    }

    private static void endBg(BufferedWriter writer) throws IOException {
        writer.write("|===\n");
    }

    private static void endline(BufferedWriter writer) throws IOException {
        writer.write("|===\n\n");
    }

    private static String too() {
        return get("to");
    }

    private static String fromm() {
        return get("from");
    }

    private static void abzac(BufferedWriter writer) throws IOException {
        endBg(writer);
    }
}
