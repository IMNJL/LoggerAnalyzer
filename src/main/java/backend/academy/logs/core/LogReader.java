package backend.academy.logs.core;

import backend.academy.logs.analyzer.Config;
import backend.academy.logs.utils.HttpFileDownloader;
import lombok.Getter;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LogReader {
    private final LogParser parser;
    @Getter public static List<String> logFilesPaths = new ArrayList<>(); // Хранение путей лог-файлов

    public LogReader(LogParser parser) {
        this.parser = parser;
    }

    public List<LogEntry> readLogs(String filePath) throws IOException {
        if (new File(filePath).isDirectory()) {
            try (Stream<Path> paths = Files.walk(Paths.get(filePath))) {
                return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".log"))
                    .flatMap(path -> {
                        try {
                            return readFromFile(path.toString()).stream();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    })
                    .toList();
            }
        } else {
            return readFromFile(filePath);
        }
    }

    private List<LogEntry> readFromFile(String filePath) throws IOException {
        try (BufferedReader reader = filePath.startsWith("http")
            ? new BufferedReader(new InputStreamReader(HttpFileDownloader.downloadAsStream(filePath)))
            : new BufferedReader(new FileReader(filePath))) {

            return reader.lines()
                .map(parser::parse)   // Парсим каждую строку
                .filter(log -> log != null) // Отфильтровываем null-значения
                .toList(); // Собираем результат в список
        }
    }
}
