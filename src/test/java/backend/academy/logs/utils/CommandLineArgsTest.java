package backend.academy.logs.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandLineArgsTest {

    @Test
    void testParseArgs_ValidArgs() {
        // Arrange
        String[] args = {"--path", "src/main/resources/log_files/", "--format", "json"};

        // Act
        CommandLineArgs.parseArgs(args);

        // Assert
        assertEquals("src/main/resources/log_files/", CommandLineArgs.get("path"));
        assertEquals("json", CommandLineArgs.get("format"));
    }

    @Test
    void testParseArgs_MissingValue() {
        // Arrange
        String[] args = {"--path", "src/main/resources/log_files/", "--format"};

        // Act
        CommandLineArgs.parseArgs(args);

        // Assert
        assertEquals("src/main/resources/log_files/", CommandLineArgs.get("path"));
        assertNull(CommandLineArgs.get("format"), "Format should be null because it has no value");
    }

    @Test
    void testParseArgs_MissingArguments() {
        // Arrange
        String[] args = {"--path"};

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> CommandLineArgs.parseArgs(args),
            "Should throw IllegalArgumentException when arguments are missing");
    }

    @Test
    void testParseArgs_InvalidFormat() {
        // Arrange
        String[] args = {"--path", "src/main/resources/log_files/", "--format"};

        // Act
        CommandLineArgs.parseArgs(args);

        // Assert
        assertEquals("src/main/resources/log_files/", CommandLineArgs.get("path"));
        assertNull(CommandLineArgs.get("format"), "Format should be null due to missing value");
    }

    @Test
    void testParseArgs_EmptyArgs() {
        // Arrange
        String[] args = {};

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> CommandLineArgs.parseArgs(args),
            "Should throw IllegalArgumentException when no arguments are provided");
    }

    @Test
    void testParseArgs_MultipleArgs() {
        // Arrange
        String[] args = {"--path", "src", "--format", "json", "--from", "2024-01-01", "--to", "2024-12-31"};

        // Act
        CommandLineArgs.parseArgs(args);

        // Assert
        assertEquals("src", CommandLineArgs.get("path"));
        assertEquals("json", CommandLineArgs.get("format"));
        assertEquals("2024-01-01", CommandLineArgs.get("from"));
        assertEquals("2024-12-31", CommandLineArgs.get("to"));
    }

    @Test
    void testParseArgs_KeyWithoutValue() {
        // Arrange
        String[] args = {"--path", "--format"};

        // Act
        CommandLineArgs.parseArgs(args);

        // Assert
        assertNull(CommandLineArgs.get("path"), "Path should be null because it has no value");
        assertNull(CommandLineArgs.get("format"), "Format should be null because it has no value");
    }

}
