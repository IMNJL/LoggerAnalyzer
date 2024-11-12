package backend.academy.logs;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class LogProcessor {

    // Метод для чтения лог-файла из локального пути или URL
    public List<LogEntry> readLogs(String pathOrUrl) throws IOException {
        if (pathOrUrl.startsWith("http")) {
            return readLogsFromUrl(pathOrUrl); // Чтение с URL
        } else {
            return readLogsFromFile(pathOrUrl); // Чтение с локального файла
        }
    }

    private List<LogEntry> readLogsFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        List<LogEntry> logs = Files.lines(path)
            .map(LogParser::parseLogLine) // Предполагается, что LogParser умеет парсить строки лога
            .collect(Collectors.toList());
        return logs;
    }

    private List<LogEntry> readLogsFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        try (Scanner scanner = new Scanner(url.openStream())) {
            List<LogEntry> logs = new ArrayList<>();
            while (scanner.hasNextLine()) {
                logs.add(LogParser.parseLogLine(scanner.nextLine()));
            }
            return logs;
        }
    }

    // Метод для фильтрации логов по времени
    public List<LogEntry> filterLogsByTime(List<LogEntry> logs, String from, String to) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime fromDate = from != null ? LocalDateTime.parse(from, formatter) : null;
        LocalDateTime toDate = to != null ? LocalDateTime.parse(to, formatter) : null;

        return logs.stream()
            .filter(log -> {
                LocalDateTime logDate = LocalDateTime.parse(log.timestamp());
                boolean isAfterFrom = fromDate == null || !logDate.isBefore(fromDate);
                boolean isBeforeTo = toDate == null || !logDate.isAfter(toDate);
                return isAfterFrom && isBeforeTo;
            })
            .collect(Collectors.toList());
    }

    // Метод для генерации отчета в Markdown
    public String generateReport(List<LogEntry> logs, String format) {
        LogParser logParser = new LogParser();
        LogReader logReader = new LogReader(logParser);

        LogAnalyzer logAnalyzer = new LogAnalyzer(logs);

        if ("markdown".equalsIgnoreCase(format)) {
            return ReportGenerator.generateMarkdownReport(logAnalyzer);
//        } else if ("adoc".equalsIgnoreCase(format)) {
//            return ReportGenerator.generateAsciiDocReport(logAnalyzer);
        } else {
            throw new IllegalArgumentException("Invalid format. Supported formats are: markdown, adoc");
        }
    }

    public static void main(String[] args) {
        LogProcessor logProcessor = new LogProcessor();

        String pathOrUrl = "src/main/resources/log_files/log.log";  // Пример локального пути или URL
        String from = "2024-08-01T00:00:00"; // Пример начальной даты
        String to = "2024-08-31T23:59:59";   // Пример конечной даты
        String format = "markdown"; // Формат вывода: markdown или adoc

        try {
            // Чтение логов
            List<LogEntry> logs = logProcessor.readLogs(pathOrUrl);

            // Фильтрация по времени
            logs = logProcessor.filterLogsByTime(logs, from, to);

            // Генерация отчета
            String report = logProcessor.generateReport(logs, format);

            // Вывод отчета
            System.out.println(report);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
