package backend.academy.logs.analyzer;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Config {
    String path;
    String format;
    String from;
    String to;
}
