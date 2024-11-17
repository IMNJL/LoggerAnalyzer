package backend.academy.logs.core;

import backend.academy.logs.utils.HttpFileDownloader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;

public class LogReader {
    private final LogParser parser;
    @Getter public final List<String> processedFiles = new ArrayList<>(); // Хранение путей лог-файлов

    public LogReader(LogParser parser) {
        this.parser = parser;
    }

    public List<LogEntry> readLogs(String filePath) throws IOException {
        processedFiles.clear(); // Очищаем список перед началом обработки
        if (new File(filePath).isDirectory()) {
            try (Stream<Path> paths = Files.walk(Paths.get(filePath))) {
                return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".log"))
                    .flatMap(path -> {
                        try {
                            processedFiles.add(path.getFileName().toString()); // Добавляем имя файла
                            return readFromFile(path.toString()).stream();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    })
                    .toList();
            }
        } else {
            processedFiles.add(new File(filePath).getName()); // Добавляем имя файла
            return readFromFile(filePath);
        }
    }

    private List<LogEntry> readFromFile(String filePath) throws IOException {
        try (BufferedReader reader = filePath.startsWith("http")
            ? new BufferedReader(new InputStreamReader(HttpFileDownloader
            .downloadAsStream(filePath), StandardCharsets.UTF_8))
            : new BufferedReader(new InputStreamReader(Files
            .newInputStream(Paths.get(filePath)), StandardCharsets.UTF_8))) {

            return reader.lines()
                .map(parser::parse)   // Парсим каждую строку
                .filter(log -> log != null) // Отфильтровываем null-значения
                .toList(); // Собираем результат в список
        }
    }
}
