package backend.academy.logs;

import backend.academy.logs.utils.CommandLineArgs;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.*;

public class MainTest {

    @Test
    public void testStartWithValidArgs() {
        // Setup mock for CommandLineArgs static methods
        String path = "/valid/path/to/logs";
        String format = "markdown";
        String from = "2024-01-01";
        String to = "2024-12-31";

        // Mock CommandLineArgs.get() method using mockStatic
        try (MockedStatic<CommandLineArgs> commandLineArgsMock = mockStatic(CommandLineArgs.class)) {
            // Define mock behavior
            commandLineArgsMock.when(() -> CommandLineArgs.get("path")).thenReturn(path);
            commandLineArgsMock.when(() -> CommandLineArgs.get("format")).thenReturn(format);
            commandLineArgsMock.when(() -> CommandLineArgs.get("from")).thenReturn(from);
            commandLineArgsMock.when(() -> CommandLineArgs.get("to")).thenReturn(to);

            // Mock Logger
            Logger mockLogger = mock(Logger.class);
            try (MockedStatic<LoggerFactory> loggerFactoryMock = mockStatic(LoggerFactory.class)) {
                loggerFactoryMock.when(() -> LoggerFactory.getLogger(Main.class)).thenReturn(mockLogger);

                // Call start method
                Main.start();

                // Verify that logger was called with the correct values
                verify(mockLogger).info("Путь к файлу логов: {}", path);
                verify(mockLogger).info("Формат отчета: {}", format);
                verify(mockLogger).info("Дата 'от': {}", from);
                verify(mockLogger).info("Дата 'до': {}", to);
            }
        }
    }

    @Test
    public void testStartWithMissingPath() {
        // Mock static method CommandLineArgs.get()
        try (MockedStatic<CommandLineArgs> mockedArgs = mockStatic(CommandLineArgs.class)) {
            // Setup behavior for static methods
            mockedArgs.when(() -> CommandLineArgs.get("path")).thenReturn(null); // Simulate missing "path"
            mockedArgs.when(() -> CommandLineArgs.get("format")).thenReturn("markdown");
            mockedArgs.when(() -> CommandLineArgs.get("from")).thenReturn("2024-01-01");
            mockedArgs.when(() -> CommandLineArgs.get("to")).thenReturn("2024-12-31");

            // Mock Logger
            Logger mockLogger = mock(Logger.class);
            when(LoggerFactory.getLogger(Main.class)).thenReturn(mockLogger);

            // Call the start() method
            Main.start();

            // Verify that the error log for missing "path" is logged
            verify(mockLogger).error("Ошибка: Параметр '--path' обязателен!");
        }
    }
}
