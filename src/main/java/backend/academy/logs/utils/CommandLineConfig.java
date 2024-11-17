package backend.academy.logs.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommandLineConfig {
    @Getter private static String outputDir = "src/main/resources/log_files";
    @Getter @Setter private static String markdownPath = "src/main/resources/reports/report.md";
    @Getter @Setter private static String asciidocPath = "src/main/resources/reports/report.adoc";
}
