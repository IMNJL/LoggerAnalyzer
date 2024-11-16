package backend.academy.logs.analyzer;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Config {
    @Getter private static final String outputDir = "src/main/resources/log_files";
    String path;
    String format;
    String from;
    String to;
}
