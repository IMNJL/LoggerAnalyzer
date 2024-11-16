package backend.academy.logs.core;

import backend.academy.logs.analyzer.Config;
import backend.academy.logs.utils.HttpFileDownloader;
import lombok.Getter;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LogReader {
    private final LogParser parser;
    // Возвращаем список путей лог-файлов
    @Getter public static List<String> logFilesPaths = new ArrayList<>(); // Хранение путей лог-файлов

    public LogReader(LogParser parser) {
        this.parser = parser;
    }

    public List<LogEntry> readLogs(String filePath) throws IOException {
        List<LogEntry> logs = new ArrayList<>();

        // Если путь указывает на директорию
        File file = new File(filePath);
        if (file.isDirectory()) {
            // Получаем все файлы .log в директории
            List<File> logFiles = Files.walk(file.toPath())
                .filter(path -> path.toString().endsWith(".log"))
                .map(Path::toFile)
                .collect(Collectors.toList());

            logFilesPaths = logFiles.stream()
                .map(File::toPath) // Преобразуем File в Path
                .map(path -> path.toAbsolutePath().normalize()) // Нормализуем путь
                .map(path -> {
                    Path basePath = new File("src").toPath().toAbsolutePath().normalize();
                    return basePath.relativize(path).toString(); // Преобразуем к относительному пути
                })
                .collect(Collectors.toList());

            // Читаем каждый файл
            for (File logFile : logFiles) {
                logs.addAll(readFromFile(logFile.getAbsolutePath()));
            }
        } else {
            // Если путь не директория, читаем как файл
            logs.addAll(readFromFile(filePath));
            logFilesPaths.add(filePath);
        }

        return logs;
    }

    private List<LogEntry> readFromFile(String filePath) throws IOException {
        BufferedReader reader;
        String outputDir = Config.outputDir();

        switch (getFileType(filePath)) {
            case "URL":
                File downloadedFile = HttpFileDownloader.download(filePath, outputDir);
                reader = new BufferedReader(new FileReader(downloadedFile));
                break;
            case "FILE":
                reader = new BufferedReader(new FileReader(filePath));
                break;
            default:
                throw new IllegalArgumentException("Unsupported file type: " + filePath);
        }

        List<LogEntry> logs = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            LogEntry log = parser.parse(line);
            if (log != null) logs.add(log);
        }
        reader.close();
        return logs;
    }

    private String getFileType(String filePath) {
        if (filePath.startsWith("http")) {
            return "URL";
        } else if (new File(filePath).exists()) {
            return "FILE";
        }
        return "UNKNOWN";
    }
}
