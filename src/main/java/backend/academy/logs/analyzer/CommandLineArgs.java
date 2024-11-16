package backend.academy.logs.analyzer;

import java.util.HashMap;
import java.util.Map;

public class CommandLineArgs {
    private static final Map<String, String> argsMap = new HashMap<>();

    public static void parseArgs(String[] args) {
        if (args.length < 2)
            throw new IllegalArgumentException();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("--")) {
                String key = arg.substring(2); // Убираем префикс "--"
                String value = (i + 1 < args.length && !args[i + 1].startsWith("--")) ? args[i + 1] : null;
                argsMap.put(key, value);
                if (value != null) i++; // Пропускаем следующий элемент, если он был значением
            }
        }
    }

    public static String get(String key) {
        return argsMap.get(key);
    }
}
