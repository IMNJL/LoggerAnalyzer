package backend.academy.logs;

import backend.academy.logs.analyzer.CommandLineArgs;
import backend.academy.logs.analyzer.Config;
import backend.academy.logs.core.LogAnalyzer;
import backend.academy.logs.core.LogParser;
import backend.academy.logs.core.LogReader;
import backend.academy.logs.core.ReportGenerator;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@UtilityClass
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        // Парсим аргументы командной строки
        CommandLineArgs.parseArgs(args);

        String md = Config.markdownPath();
        String ascii = Config.asciidocPath();

        // Получаем значения аргументов
        String path = CommandLineArgs.get("path");
        String format = CommandLineArgs.get("format");
        String from = CommandLineArgs.get("from");
        String to = CommandLineArgs.get("to");

        // Проверка обязательного параметра path
        if (path == null) {
            LOGGER.info("Ошибка: Параметр '--path' обязателен!");
            return;
        }


        if (format == null) {
            format = "markdown";
        } else if (!format.equals("adoc") && !format.equals("md")) {
            LOGGER.error("Ошибка: Параметр '--format' должен быть 'adoc' или 'md'!");
            return;
        }

        LOGGER.info("Путь к файлу логов: " + path);
        LOGGER.info("Формат отчета: " + format);
        LOGGER.info("Дата 'от': " + from);
        LOGGER.info("Дата 'до': " + to);

        // Дальнейшая логика анализа логов
        try {
            // Создание объектов для парсинга, чтения и анализа логов
            LogParser parser = new LogParser();
            LogReader reader = new LogReader(parser);
            LogAnalyzer analyzer = new LogAnalyzer(reader.readLogs(path));

            if (format.equals("adoc")) {
                ReportGenerator.generateAsciiDocReport(analyzer, ascii);
            } else {
                ReportGenerator.generateMarkdownReport(analyzer, md);
            }

        } catch (Exception e) {
            // Обработка ошибок
            e.printStackTrace();
            LOGGER.error("Произошла ошибка: " + e.getMessage());
        }
    }
}
