package backend.academy.logs.core;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class LogAnalyzer {
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
//public OptionalDouble getResponseSizePercentile(int percentile) {
//    // Проверяем на пустой список и некорректный процент
//    if (logs.isEmpty() || percentile <= 0 || percentile > 100) {
//        return OptionalDouble.empty(); // Возвращаем Optional.empty() для обозначения отсутствия значения
//    }
//
//    // Сортируем список по убыванию размера
//    List<Long> sortedSizes = logs.stream()
//        .mapToLong(LogEntry::size)  // Преобразуем в поток примитивных long
//        .sorted()  // Сортируем по возрастанию
//        .boxed()  // Преобразуем long в Long (обертка)
//        .toList();  // Собираем в List<Long>
//
//    // Рассчитываем индекс по формуле
//    int index = (int) Math.floor((percentile / 100.0) * (sortedSizes.size() - 1));
//
//    // Возвращаем значение на найденном индексе
//    return OptionalDouble.of(sortedSizes.get(index));
//}

    // Подсчет самых частых запрашиваемых ресурсов
    public Map<String, Long> getMostFrequentResources() {
        return logs.stream()
            .collect(Collectors.groupingBy(LogEntry::request, Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(10)
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
}
