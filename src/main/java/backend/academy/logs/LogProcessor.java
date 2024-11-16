//package backend.academy.logs;
//
//import backend.academy.logs.core.LogAnalyzer;
//import backend.academy.logs.core.ReportGenerator;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import java.io.IOException;
//import java.net.URL;
//import java.nio.file.*;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.stream.Stream;
//
//public class LogProcessor {
//    private static final Logger LOGGER = LoggerFactory.getLogger(LogProcessor.class);
//
//    // Чтение логов из локального файла, URL или шаблонов пути
//    public List<LogEntry> readLogs(String pathOrPattern) throws IOException {
//        if (pathOrPattern.startsWith("http")) {
//            return readLogsFromUrl(pathOrPattern);
//        } else {
//            return readLogsFromFiles(pathOrPattern);
//        }
//    }
//
//    private List<LogEntry> readLogsFromFiles(String pattern) throws IOException {
//        List<LogEntry> logs = new ArrayList<>();
//        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
//        try (Stream<Path> paths = Files.walk(Paths.get("."))) {
//            paths.filter(Files::isRegularFile)
//                .filter(path -> matcher.matches(path.getFileName()))
//                .forEach(path -> {
//                    try {
//                        logs.addAll(Files.lines(path).map(LogParser::parseLogLine).filter(Objects::nonNull).toList());
//                    } catch (IOException e) {
//                        System.err.println("Error reading file: " + path + ", " + e.getMessage());
//                    }
//                });
//        }
//        return logs;
//    }
//
//    private List<LogEntry> readLogsFromUrl(String urlString) throws IOException {
//        URL url = new URL(urlString);
//        try (Scanner scanner = new Scanner(url.openStream())) {
//            return scanner.tokens().map(LogParser::parseLogLine).filter(Objects::nonNull).toList();
//        }
//    }
//
//    public List<LogEntry> filterLogsByTime(String fromArg, String toArg) {
//        List<LogEntry> filteredLogs = new ArrayList<>();
//        try {
//            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
//            LocalDate fromDate = LocalDate.parse(fromArg.trim(), formatter);
//            LocalDate toDate = LocalDate.parse(toArg.trim(), formatter);
//
//            LOGGER.info("Parsed 'from' date: {}", fromDate);
//            LOGGER.info("Parsed 'to' date: {}", toDate);
//
//            for (LogEntry log : readLogs(Config.filePath())) {
//                LocalDate logDate = LocalDate.parse(log.timestamp());
//                if ((logDate.isEqual(fromDate) || logDate.isAfter(fromDate)) &&
//                    (logDate.isEqual(toDate) || logDate.isBefore(toDate))) {
//                    filteredLogs.add(log);
//                }
//            }
//        } catch (Exception e) {
//            LOGGER.error("Error parsing dates or filtering logs: {}", e.getMessage(), e);
//        }
//        return filteredLogs;
//    }
//
//    // Генерация отчета
//    public String generateReport(List<LogEntry> logs, String format) {
//        LogAnalyzer analyzer = new LogAnalyzer(logs);
//        return switch (format.toLowerCase()) {
//            case "markdown" -> ReportGenerator.generateMarkdownReport(analyzer);
//            case "adoc" -> ReportGenerator.generateAsciiDocReport(analyzer);
//            default -> throw new IllegalArgumentException("Unsupported format: " + format);
//        };
//    }
//}
