package backend.academy.logs.core;

import java.io.*;
import java.util.Map;
import static backend.academy.logs.analyzer.CommandLineArgs.get;
import static backend.academy.logs.analyzer.Config.getStatusName;

public class ReportGenerator {

    public static void generateMarkdownReport(LogAnalyzer analyzer, String outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            // Раздел: Общая информация
            writer.write("#### Общая информация\n\n");
            writer.write(String.format("| %-21s | %-16s |\n", "Метрика", "Значение"));
            writer.write("|:---------------------:|-----------------:|\n");
            writer.write(String.format("| %-21s | %12s     |\n", "Начальная дата", get("from")));
            writer.write(String.format("| %-21s | %12s     |\n", "Конечная дата", get("to")));
            writer.write(String.format("| %-21s | %,16d |\n", "Количество запросов", analyzer.getTotalRequests()));
            writer.write(String.format("| %-21s | %,11.0f байт |\n", "Средний размер ответа", analyzer.getAverageResponseSize()));
            writer.write(String.format("| %-21s | %,12.0f байт |\n\n", "95p размера ответа", analyzer.getResponseSizePercentile(95)));

            // Раздел: Запрашиваемые ресурсы
            writer.write("\n#### Запрашиваемые ресурсы\n\n");
            writer.write(String.format("| %-33s | %-12s |\n", "Ресурс", "Количество"));
            writer.write("|:---------------------------------:|-------------:|\n");
            for (Map.Entry<String, Long> entry : analyzer.getMostFrequentResources().entrySet()) {
                writer.write(String.format("| %-33s | %,12d |\n", entry.getKey(), entry.getValue()));
            }

            // Раздел: Коды ответа
            writer.write("\n#### Коды ответа\n\n");
            writer.write(String.format("| %-5s | %-21s | %-12s |\n", "Код", "Имя", "Количество"));
            writer.write("|:-----:|:---------------------:|-------------:|\n");
            for (Map.Entry<Integer, Long> entry : analyzer.getResponseCodeCounts().entrySet()) {
                writer.write(String.format("| %-5d | %-21s | %,12d |\n", entry.getKey(), getStatusName(entry.getKey()), entry.getValue()));
            }
        }
    }

    public static void generateAsciiDocReport(LogAnalyzer analyzer, String outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            // Раздел: Общая информация
            writer.write("== Общая информация\n\n");
            writer.write("|===\n");
            writer.write("| Метрика | Значение\n");
            writer.write("|:--------|:--------\n");
            writer.write(String.format("| Начальная дата | %s\n", get("from")));
            writer.write(String.format("| Конечная дата | %s\n", get("to")));
            writer.write(String.format("| Количество запросов | %,d\n", analyzer.getTotalRequests()));
            writer.write(String.format("| Средний размер ответа | %,11.0f байт\n", analyzer.getAverageResponseSize()));
            writer.write(String.format("| 95p размера ответа | %,12.0f байт\n\n", analyzer.getResponseSizePercentile(95)));

            // Раздел: Запрашиваемые ресурсы
            writer.write("== Запрашиваемые ресурсы\n\n");
            writer.write("|===\n");
            writer.write("| Ресурс | Количество\n");
            writer.write("|:-------|:---------\n");
            for (Map.Entry<String, Long> entry : analyzer.getMostFrequentResources().entrySet()) {
                writer.write(String.format("| %s | %,d\n", entry.getKey(), entry.getValue()));
            }

            // Раздел: Коды ответа
            writer.write("\n== Коды ответа\n\n");
            writer.write("|===\n");
            writer.write("| Код | Имя | Количество\n");
            writer.write("|:----|:----|:---------\n");
            for (Map.Entry<Integer, Long> entry : analyzer.getResponseCodeCounts().entrySet()) {
                writer.write(String.format("| %d | %s | %,d\n", entry.getKey(), getStatusName(entry.getKey()), entry.getValue()));
            }
            writer.write("|===\n");
        }
    }
}
