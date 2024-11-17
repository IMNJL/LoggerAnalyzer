package backend.academy.logs.utils;

import backend.academy.logs.analyzer.LogAnalyzer;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import lombok.experimental.UtilityClass;
import static backend.academy.logs.utils.CommandLineArgs.get;
import static backend.academy.logs.utils.Config.getStatusName;

@UtilityClass
public class ReportGenerator {
    private static final int PERCENTILE = 95;

    public static void generateMarkdownReport(LogAnalyzer analyzer) throws IOException {
        Path outputPath = Paths.get(CommandLineConfig.markdownPath()).normalize();

        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
            // Раздел: Общая информация
            writer.write(String.format("#### Общая информация%n%n"));
            writer.write(String.format("| %-21s | %-16s |%n", "Метрика", "Значение"));
            writer.write("|:---------------------:|-----------------:|%n");
            String string = "| %-21s | %12s     |%n";
            writer.write(String.format(string, "Начальная дата", fromm()));
            writer.write(String.format(string, "Конечная дата", too()));
            writer.write(String.format("| %-21s | %,16d |%n", "Количество запросов", analyzer.getTotalRequests()));
            writer.write(String.format("| %-21s | %,11.0f байт |%n", "Средний размер ответа",
                analyzer.getAverageResponseSize()));
            writer.write(String.format("| %-21s | %,12.0f байт |%n%n", "95p размера ответа",
                analyzer.getResponseSizePercentile(PERCENTILE)));

            // Раздел: Запрашиваемые ресурсы
            writer.write(String.format("%n#### Запрашиваемые ресурсы%n%n"));
            writer.write(String.format("| %-33s | %-12s |%n", "Ресурс", "Количеcтво"));
            writer.write("|:---------------------------------:|-------------:|%n");
            for (Map.Entry<String, Long> entry : analyzer.getMostFrequentResources().entrySet()) {
                writer.write(String.format("| %-33s | %,12d |%n", entry.getKey(), entry.getValue()));
            }

            // Раздел: Коды ответа
            writer.write(String.format("%n#### Коды ответа%n%n"));
            writer.write(String.format("| %-5s | %-21s | %-12s |%n", "Код", "Имя", "Количество"));
            writer.write("|:-----:|:---------------------:|-------------:|%n");
            for (Map.Entry<Integer, Long> entry : analyzer.getResponseCodeCounts().entrySet()) {
                writer.write(String.format("| %-5d | %-21s | %,12d |%n", entry.getKey(),
                    getStatusName(entry.getKey()), entry.getValue()));
            }
        }
    }

    public static void generateAsciiDocReport(LogAnalyzer analyzer) throws IOException {
        Path outputPath = Paths.get(CommandLineConfig.asciidocPath()).normalize();
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
            // Раздел: Общая информация
            writer.write(String.format("== Общая информация%n%n"));
            abzac(writer);
            writer.write(String.format("| Метрика | Значение%n"));
            writer.write(String.format("|:--------|:--------%n"));
            writer.write(String.format("| Начальная дата | %s%n", fromm()));
            writer.write(String.format("| Конечная дата | %s%n", too()));
            writer.write(String.format("| Количество запросов | %,d%n", analyzer.getTotalRequests()));
            writer.write(String.format("| Средний размер ответа | %,11.0f байт%n", analyzer.getAverageResponseSize()));
            writer.write(String.format("| 95p размера ответа | %,12.0f байт%n%n", analyzer.getResponseSizePercentile(
                PERCENTILE)));

            // Раздел: Запрашиваемые ресурсы
            writer.write(String.format("== Запрашиваемые ресурсы%n%n"));
            abzac(writer);
            writer.write(String.format("| Ресурс | Количество%n"));
            writer.write(String.format("|:-------|:---------%n"));
            for (Map.Entry<String, Long> entry : analyzer.getMostFrequentResources().entrySet()) {
                writer.write(String.format("| %s | %,d%n", entry.getKey(), entry.getValue()));
            }

            // Раздел: Коды ответа
            writer.write(String.format("%n== Коды ответа%n%n"));
            abzac(writer);
            writer.write(String.format("| Код | Имя | Кол-во%n"));
            writer.write(String.format("|:----|:----|:---------%n"));
            for (Map.Entry<Integer, Long> entry : analyzer.getResponseCodeCounts().entrySet()) {
                writer.write(String.format("| %d | %s | %,d%n", entry.getKey(),
                    getStatusName(entry.getKey()), entry.getValue()));
            }
            abzac(writer);
        }
    }

    private static String too() {
        return get("to");
    }

    private static String fromm() {
        return get("from");
    }

    private static void abzac(BufferedWriter writer) throws IOException {
        writer.write("|===\n");
    }
}
