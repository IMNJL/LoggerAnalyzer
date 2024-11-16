package backend.academy.logs.core;

import backend.academy.logs.utils.HttpFileDownloader;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LogReader {
    private final LogParser parser;

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

            // Читаем каждый файл
            for (File logFile : logFiles) {
                logs.addAll(readFromFile(logFile.getAbsolutePath()));
            }
        } else {
            // Если путь не директория, читаем как файл
            logs.addAll(readFromFile(filePath));
        }

        return logs;
    }

    private List<LogEntry> readFromFile(String filePath) throws IOException {
        BufferedReader reader;
        if (filePath.startsWith("http")) {
            reader = HttpFileDownloader.download(filePath);
        } else {
            reader = new BufferedReader(new FileReader(filePath));
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
}
