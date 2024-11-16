package backend.academy.logs.core;

import java.util.Map;
import static backend.academy.logs.analyzer.CommandLineArgs.get;
import static backend.academy.logs.core.LogReader.logFilesPaths;

public class ReportGenerator {

    // Метод для генерации отчета в формате Markdown
    public static String generateMarkdownReport(LogAnalyzer analyzer) {
        StringBuilder report = new StringBuilder();

        // Раздел: Общая информация
        report.append("#### Общая информация\n\n");
        report.append(String.format("| %-21s | %-16s |\n", "Метрика", "Значение"));
        report.append("|:---------------------:|-----------------:|\n");
        for (String filePath : logFilesPaths()) {
            report.append(String.format("| %-21s | %-24s |\n", STR."Файл \{1 + logFilesPaths.indexOf(filePath)}", filePath));
        }
        report.append(String.format("| %-21s | %12s     |\n", "Начальная дата", get("from")));
        report.append(String.format("| %-21s | %12s     |\n", "Конечная дата", get("to")));
        report.append(String.format("| %-21s | %,16d |\n", "Количество запросов", analyzer.getTotalRequests()));
        report.append(
            String.format("| %-21s | %,11.0f байт |\n", "Средний размер ответа", analyzer.getAverageResponseSize()));
        report.append(String.format("| %-21s | %,12.0f байт |\n\n", "95p размера ответа",
            analyzer.getResponseSizePercentile(95)));

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

    public static String generateAsciiDocReport(LogAnalyzer analyzer) {
        StringBuilder report = new StringBuilder();

        // Раздел: Общая информация
        report.append("== Общая информация\n\n");
        report.append("|===\n");
        report.append("| Метрика | Значение\n");
        report.append("|:--------|:--------\n");
        report.append(String.format("| Файл(-ы)/ресурсы | %s\n", get("path")));  // Предположим, что filePath хранит путь к логам
        report.append(String.format("| Начальная дата | %s\n",
            get("from"))); // Получаем начальную дату через метод в LogAnalyzer
        report.append(String.format("| Конечная дата | %s\n",
            get("to"))); // Получаем конечную дату через метод в LogAnalyzer
        report.append(
            String.format("| Количество запросов | %,d\n", analyzer.getTotalRequests())); // Количество запросов
        report.append(String.format("| Средний размер ответа | %,11.0f байт\n",
            analyzer.getAverageResponseSize())); // Средний размер ответа
        report.append(String.format("| 95p размера ответа | %,12.0f байт\n\n",
            analyzer.getResponseSizePercentile(95))); // 95-й процентиль размера ответа

        // Раздел: Запрашиваемые ресурсы
        report.append("== Запрашиваемые ресурсы\n\n");
        report.append("|===\n");
        report.append("| Ресурс | Количество\n");
        report.append("|:-------|:---------\n");
        Map<String, Long> resources =
            analyzer.getMostFrequentResources();  // Получаем наиболее часто запрашиваемые ресурсы
        for (Map.Entry<String, Long> entry : resources.entrySet()) {
            report.append(String.format("| %s | %,d\n", entry.getKey(),
                entry.getValue()));  // Форматирование вывода для каждого ресурса
        }

        // Раздел: Коды ответа
        report.append("\n== Коды ответа\n\n");
        report.append("|===\n");
        report.append("| Код | Имя | Количество\n");
        report.append("|:----|:----|:---------\n");
        Map<Integer, Long> responseCodes = analyzer.getResponseCodeCounts();  // Получаем количество ответов по кодам
        for (Map.Entry<Integer, Long> entry : responseCodes.entrySet()) {
            String statusName = getStatusName(entry.getKey());  // Метод для получения имени кода ответа
            report.append(String.format("| %d | %s | %,d\n", entry.getKey(), statusName,
                entry.getValue()));  // Форматируем вывод для каждого кода ответа
        }

        report.append("|===\n");

        return report.toString();  // Возвращаем строку отчета в AsciiDoc
    }

    // Метод для получения имени кода ответа
    private static String getStatusName(int statusCode) {
        switch (statusCode / 100) {
            case 1:
                return "Informational Response";
            case 2:
                return "OK";
            case 3:
                return "Redirection";
            case 4:
                return "Client Error";
            case 5:
                return "Internal Server Error";

            default:
                return "Unknown";
        }
    }
}
