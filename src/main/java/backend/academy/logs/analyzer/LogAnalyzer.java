package backend.academy.logs.analyzer;

import backend.academy.logs.core.LogEntry;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LogAnalyzer {
    private static final int MAX_PERCENT = 100;
    private static final double MAX_PERCENT_D = 100.0;
    private static final int MAX_SIZE = 10;
    private final List<LogEntry> logs;

    public LogAnalyzer(List<LogEntry> logs) {
        this.logs = logs;
    }

    // Подсчет общего количества запросов
    public long getTotalRequests() {
        return logs.size();
    }

    // Расчет среднего размера ответа сервера
    public double getAverageResponseSize() {
        return logs.stream()
            .mapToLong(LogEntry::size)
            .average()
            .orElse(0.0);
    }

//     Расчет N-го процентиля размера ответа сервера
    public double getResponseSizePercentile(int percentile) {
        // Проверяем корректность ввода
        if (logs.isEmpty() || percentile <= 0 || percentile > MAX_PERCENT) {
            return Double.NaN; // Возвращаем NaN как обозначение некорректного результата
        }

        // Извлекаем и сортируем размеры логов
        List<Integer> sortedSizes = logs.stream()
            .map(LogEntry::size) // Извлекаем размер из каждого LogEntry
            .sorted() // Сортируем по возрастанию
            .toList(); // Преобразуем в неизменяемый список

        // Вычисляем индекс перцентиля
        int index = (int) Math.ceil((percentile / MAX_PERCENT_D) * sortedSizes.size()) - 1;

        // Гарантируем, что индекс корректен
        index = Math.max(0, Math.min(index, sortedSizes.size() - 1));

        // Возвращаем значение размера на найденном индексе
        return sortedSizes.get(index);
    }

    // Подсчет самых частых запрашиваемых ресурсов
    public Map<String, Long> getMostFrequentResources() {
        return logs.stream()
            .collect(Collectors.groupingBy(LogEntry::request, Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(MAX_SIZE)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new // Сохраняем порядок для топ-10
            ));
    }

    // Подсчет количества каждого кода ответа
    public Map<Integer, Long> getResponseCodeCounts() {
        return logs.stream()
            .collect(Collectors.groupingBy(LogEntry::status, Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new // Сохраняем порядок по коду ответа
            ));
    }

    public long getMaxResponseSize() {
        return logs.stream()
            .mapToLong(LogEntry::size)
            .max()
            .orElse(0);
    }

    public long getUniqueIpCount() {
        return logs.stream()
            .map(LogEntry::ip)
            .distinct()
            .count();
    }

    public List<LogEntry> filterLogs(Predicate<LogEntry> filter) {
        return logs.stream()
            .filter(filter)
            .toList();
    }
}
