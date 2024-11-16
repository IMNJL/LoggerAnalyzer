package backend.academy.logs;

import backend.academy.logs.analyzer.CommandLineArgs;
import backend.academy.logs.core.LogAnalyzer;
import backend.academy.logs.core.LogParser;
import backend.academy.logs.core.LogReader;
import backend.academy.logs.core.ReportGenerator;

public class Main {
    public static void main(String[] args) {
        // Парсим аргументы командной строки
        CommandLineArgs.parseArgs(args);

        // Получаем значения аргументов
        String path = CommandLineArgs.get("path");
        String format = CommandLineArgs.get("format");
        String from = CommandLineArgs.get("from");
        String to = CommandLineArgs.get("to");

        // Проверяем наличие обязательного параметра path
        if (path == null) {
            System.err.println("Ошибка: Параметр '--path' обязателен!");
            return;
        }

        System.out.println("Путь к файлу логов: " + path);
        System.out.println("Формат отчета: " + format);
        System.out.println("Дата 'от': " + from);
        System.out.println("Дата 'до': " + to);

        // Дальнейшая логика анализа логов
        try {
            LogParser parser = new LogParser();
            LogReader reader = new LogReader(parser);
            LogAnalyzer analyzer = new LogAnalyzer(reader.readLogs(path));

            String report = format.equals("adoc")
                ? ReportGenerator.generateAsciiDocReport(analyzer)
                : ReportGenerator.generateMarkdownReport(analyzer);

            System.out.println("\n" + report);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Произошла ошибка: " + e.getMessage());
        }
    }
}
