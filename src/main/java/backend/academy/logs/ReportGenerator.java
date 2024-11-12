package backend.academy.logs;

import java.util.Map;
import static backend.academy.logs.Config.filePath;

public class ReportGenerator {

    // Метод для генерации отчета в формате Markdown
    public static String generateMarkdownReport(LogAnalyzer analyzer) {
        StringBuilder report = new StringBuilder();

        // Раздел: Общая информация
        report.append("#### Общая информация\n\n");
        report.append(String.format("| %-21s | %-16s |\n", "Метрика", "Значение"));
        report.append("|:---------------------:|-----------------:|\n");
        report.append(String.format("| %-21s | %12s     |\n", "Файл(-ы)", filePath));
        report.append(String.format("| %-21s | %12s     |\n", "Начальная дата", "31.08.2024"));
        report.append(String.format("| %-21s | %12s     |\n", "Конечная дата", "-"));
        report.append(String.format("| %-21s | %,16d |\n", "Количество запросов", analyzer.getTotalRequests()));
        report.append(String.format("| %-21s | %,11.0f байт |\n", "Средний размер ответа", analyzer.getAverageResponseSize()));
//        report.append(String.format("| %-21s | %,12.0f байт |\n\n", "95p размера ответа", analyzer.getResponseSizePercentile(95)));

        // Раздел: Запрашиваемые ресурсы
        report.append("\n#### Запрашиваемые ресурсы\n\n");
        report.append(String.format("| %-33s | %-12s |\n", "Ресурс", "Количество"));
        report.append("|:---------------------------------:|-------------:|\n");
        Map<String, Long> resources = analyzer.getMostFrequentResources();
        for (Map.Entry<String, Long> entry : resources.entrySet()) {
            report.append(String.format("| %-17s | %,12d |\n", entry.getKey(), entry.getValue()));
        }

        // Раздел: Коды ответа
        report.append("\n#### Коды ответа\n\n");
        report.append(String.format("| %-5s | %-21s | %-12s |\n", "Код", "Имя", "Количество"));
        report.append("|:-----:|:---------------------:|-------------:|\n");
        Map<Integer, Long> responseCodes = analyzer.getResponseCodeCounts();
        for (Map.Entry<Integer, Long> entry : responseCodes.entrySet()) {
            String statusName = getStatusName(entry.getKey());  // Метод для получения имени кода ответа
            report.append(String.format("| %-5d | %-21s | %,12d |\n", entry.getKey(), statusName, entry.getValue()));
        }

        return report.toString();
    }

    // Метод для получения имени кода ответа
    private static String getStatusName(int statusCode) {
        switch (statusCode/100) {
            case 1: return "Informational Response";
            case 2: return "OK";
            case 3: return "Redirection";
            case 4: return "Client Error";
            case 5: return "Internal Server Error";

            default: return "Unknown";
        }
    }
}
