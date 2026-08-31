package com.dreamportal.ai;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Env {

    private static final Map<String, String> FILE_VARS = loadFile();

    public static String get(String name) {
        String sys = System.getenv(name);
        if (sys != null && !sys.isBlank()) {
            return sys;
        }
        return FILE_VARS.get(name);
    }

    private static Map<String, String> loadFile() {
        Map<String, String> vars = new HashMap<>();
        Path p = Path.of(".env");
        if (Files.exists(p)) {
            try {
                for (String line : Files.readAllLines(p)) {
                    String trimmed = line.trim();
                    if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                        continue;
                    }
                    int eq = trimmed.indexOf('=');
                    if (eq > 0) {
                        vars.put(trimmed.substring(0, eq).trim(), trimmed.substring(eq + 1).trim());
                    }
                }
            } catch (IOException ignored) {
            }
        }
        return vars;
    }
}
