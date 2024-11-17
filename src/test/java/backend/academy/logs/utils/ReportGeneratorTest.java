package backend.academy.logs.utils;

import backend.academy.logs.analyzer.LogAnalyzer;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportGeneratorTest {

    @Test
    void testGenerateMarkdownReport_CommonSection() throws IOException {
        // Arrange
        LogAnalyzer mockAnalyzer = mock(LogAnalyzer.class);
        when(mockAnalyzer.getTotalRequests()).thenReturn(100L);
        when(mockAnalyzer.getAverageResponseSize()).thenReturn(250.0);
        when(mockAnalyzer.getResponseSizePercentile(95)).thenReturn(300.0);

        List<String> processedFiles = Arrays.asList("file1.log", "file2.log");

        // Act
        Path tempFilePath = Files.createTempFile("report", ".md");
        CommandLineConfig.markdownPath(tempFilePath.toString());
        ReportGenerator.generateMarkdownReport(mockAnalyzer, processedFiles);

        // Assert
        assertTrue(Files.exists(tempFilePath), "Markdown report file should be created.");
        try (BufferedReader reader = Files.newBufferedReader(tempFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("| Файл(-ы)")) {
                    assertTrue(line.contains("file1.log, file2.log"));
                }
                if (line.contains("| Количество запросов")) {
                    assertTrue(line.contains("100"));
                }
                if (line.contains("| Средний размер ответа")) {
                    assertTrue(line.contains("250"));
                }
                if (line.contains("| 95-й перцентиль")) {
                    assertTrue(line.contains("300"));
                }
            }
        }

        // Cleanup
        Files.delete(tempFilePath);
    }

    @Test
    void testGenerateAsciiDocReport_CommonSection() throws IOException {
        // Arrange
        LogAnalyzer mockAnalyzer = mock(LogAnalyzer.class);
        when(mockAnalyzer.getTotalRequests()).thenReturn(150L);
        when(mockAnalyzer.getAverageResponseSize()).thenReturn(200.0);
        when(mockAnalyzer.getResponseSizePercentile(95)).thenReturn(250.0);

        List<String> processedFiles = Arrays.asList("log1.txt", "log2.txt");

        // Act
        Path tempFilePath = Files.createTempFile("report", ".adoc");
        CommandLineConfig.asciidocPath(tempFilePath.toString());
        ReportGenerator.generateAsciiDocReport(mockAnalyzer, processedFiles);

        // Assert
        assertTrue(Files.exists(tempFilePath), "AsciiDoc report file should be created.");
        try (BufferedReader reader = Files.newBufferedReader(tempFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("| Файл(-ы)")) {
                    assertTrue(line.contains("log1.txt, log2.txt"));
                }
                if (line.contains("| Количество запросов")) {
                    assertTrue(line.contains("150"));
                }
                if (line.contains("| Средний размер ответа")) {
                    assertTrue(line.contains("200"));
                }
                if (line.contains("| 95-й перцентиль")) {
                    assertTrue(line.contains("250"));
                }
            }
        }

        Files.delete(tempFilePath);
    }

    @Test
    void testGenerateMarkdownReport_RequestedResources() throws IOException {
        LogAnalyzer mockAnalyzer = mock(LogAnalyzer.class);
        Map<String, Long> frequentResources = new HashMap<>();
        frequentResources.put("/index.html", 20L);
        frequentResources.put("/about.html", 15L);
        when(mockAnalyzer.getMostFrequentResources()).thenReturn(frequentResources);

        List<String> processedFiles = Arrays.asList("file1.log", "file2.log");

        Path tempFilePath = Files.createTempFile("report", ".md");
        CommandLineConfig.markdownPath(tempFilePath.toString());
        ReportGenerator.generateMarkdownReport(mockAnalyzer, processedFiles);

        assertTrue(Files.exists(tempFilePath), "Markdown report file should be created.");
        try (BufferedReader reader = Files.newBufferedReader(tempFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("| /index.html")) {
                    assertTrue(line.contains("20"));
                }
                if (line.contains("| /about.html")) {
                    assertTrue(line.contains("15"));
                }
            }
        }

        Files.delete(tempFilePath);
    }

    @Test
    void testGenerateAsciiDocReport_RequestedResources() throws IOException {
        LogAnalyzer mockAnalyzer = mock(LogAnalyzer.class);
        Map<String, Long> frequentResources = new HashMap<>();
        frequentResources.put("/home.html", 40L);
        frequentResources.put("/contact.html", 25L);
        when(mockAnalyzer.getMostFrequentResources()).thenReturn(frequentResources);

        List<String> processedFiles = Arrays.asList("log1.txt", "log2.txt");

        Path tempFilePath = Files.createTempFile("report", ".adoc");
        CommandLineConfig.asciidocPath(tempFilePath.toString());
        ReportGenerator.generateAsciiDocReport(mockAnalyzer, processedFiles);

        assertTrue(Files.exists(tempFilePath), "AsciiDoc report file should be created.");
        try (BufferedReader reader = Files.newBufferedReader(tempFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("| /home.html")) {
                    assertTrue(line.contains("40"));
                }
                if (line.contains("| /contact.html")) {
                    assertTrue(line.contains("25"));
                }
            }
        }

        Files.delete(tempFilePath);
    }
}
