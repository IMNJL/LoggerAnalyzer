package backend.academy.logs.utils;

import lombok.Getter;

public class CommandLineConfig {
    @Getter private static String outputDir = "src/main/resources/log_files";
    @Getter private static String markdownPath = "src/main/resources/reports/report.md";
    @Getter private static String asciidocPath = "src/main/resources/reports/report.adoc";

    public static void configure(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("--outputDir=")) {
                outputDir = arg.substring("--outputDir=".length());
            } else if (arg.startsWith("--markdownPath=")) {
                markdownPath = arg.substring("--markdownPath=".length());
            } else if (arg.startsWith("--asciidocPath=")) {
                asciidocPath = arg.substring("--asciidocPath=".length());
            }
        }
    }
}
