package backend.academy.logs.utils;

import java.util.HashMap;
import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommandLineArgs {
    private static final Map<String, String> ARGS_MAP = new HashMap<>();

    public static void parseArgs(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("--")) {
                String key = arg.substring(2); // Убираем префикс "--"
                String value = ((i + 1) < args.length && !args[i + 1].startsWith("--")) ? args[i + 1] : null;
                ARGS_MAP.put(key, value);
            }
        }
    }

    public static String get(String key) {
        return ARGS_MAP.get(key);
    }
}
