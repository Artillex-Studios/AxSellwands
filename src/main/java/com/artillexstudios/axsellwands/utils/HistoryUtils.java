package com.artillexstudios.axsellwands.utils;

import com.artillexstudios.axapi.libs.boostedyaml.block.implementation.Section;
import com.artillexstudios.axapi.utils.file.FileUtils;
import com.artillexstudios.axsellwands.AxSellwands;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static com.artillexstudios.axsellwands.AxSellwands.CONFIG;

public class HistoryUtils {
    private static final DateTimeFormatter FILE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static Path logsDir;
    private static boolean logToFile;
    private static boolean logToConsole;

    public static void reload() {
        Section logging = CONFIG.getSection("logging");
        logToFile = logging.getBoolean("file.enabled", true);
        logToConsole = logging.getBoolean("console.enabled", false);
        if (!logToFile) return;
        logsDir = FileUtils.getInstance().getFolder().toPath().resolve(logging.getString("file.path"));
        try {
            Files.createDirectories(logsDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void writeToHistory(String text) {
        ZonedDateTime now = ZonedDateTime.now();
        if (logToFile) {
            try {
                String line = "[%s] %s%s".formatted(now.format(TIME_FORMAT), text, System.lineSeparator());
                Path logFile = logsDir.resolve("%s.log".formatted(now.format(FILE_FORMAT)));
                Files.writeString(logFile, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (logToConsole) {
            String line = "[%s] %s".formatted(AxSellwands.getInstance().getName(), text);
            Bukkit.getConsoleSender().sendMessage(line);
        }
    }
}
