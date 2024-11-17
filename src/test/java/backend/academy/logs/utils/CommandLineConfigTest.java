package backend.academy.logs.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandLineConfigTest {

    @Test
    void testGetOutputDir() {
        // Assert
        assertEquals("src/main/resources/log_files", CommandLineConfig.outputDir(),
            "Output directory should return the correct default path");
    }

    @Test
    void testGetMarkdownPath() {
        // Assert
        assertEquals("src/main/resources/reports/report.md", CommandLineConfig.markdownPath(),
            "Markdown path should return the correct default path");
    }

    @Test
    void testGetAsciidocPath() {
        // Assert
        assertEquals("src/main/resources/reports/report.adoc", CommandLineConfig.asciidocPath(),
            "Asciidoc path should return the correct default path");
    }
}
