package backend.academy.logs;

import backend.academy.logs.analyzer.LogAnalyzer;
import backend.academy.logs.core.LogParser;
import backend.academy.logs.core.LogReader;
import backend.academy.logs.utils.CommandLineArgs;
import backend.academy.logs.utils.ReportGenerator;
import java.util.Collections;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@UtilityClass
public class Main {
    private static final String ADOC = "adoc";
    private static final String MARKDOWN = "markdown";
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        CommandLineArgs.parseArgs(args);
        start();
    }

    private static void start() {
        // Получаем значения аргументов
        String path = CommandLineArgs.get("path");
        String format = CommandLineArgs.get("format");
        String from = CommandLineArgs.get("from");
        String to = CommandLineArgs.get("to");

        // Проверка обязательного параметра path
        if (path == null) {
            LOGGER.error("Ошибка: Параметр '--path' обязателен!");
        }

        LOGGER.info("Путь к файлу логов: {}", path);
        LOGGER.info("Формат отчета: {}", format);
        LOGGER.info("Дата 'от': {}", from);
        LOGGER.info("Дата 'до': {}", to);

        if (format == null) {
            format = MARKDOWN;
        }
        if (!ADOC.equalsIgnoreCase(format) && !MARKDOWN.equalsIgnoreCase(format)) {
            LOGGER.error("Ошибка: Параметр '--format' должен быть '.adoc' или '.markdown'!");
        } else {
            try {
                // процессинг
                LogParser parser = new LogParser();
                LogReader reader = new LogReader(parser);
                LogAnalyzer analyzer = new LogAnalyzer(reader.readLogs(path));

                if (ADOC.equalsIgnoreCase(format)) {
                    ReportGenerator.generateAsciiDocReport(analyzer, Collections
                        .unmodifiableList(reader.processedFiles()));
                } else {
                    ReportGenerator.generateMarkdownReport(analyzer, Collections
                        .unmodifiableList(reader.processedFiles()));
                }

            } catch (Exception e) {
                LOGGER.error("Произошла ошибка...пупупу... {}", e.getMessage());
            }
        }
        LOGGER.info("Документ успешно создан");
    }
}
