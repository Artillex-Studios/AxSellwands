package com.artillexstudios.axsellwands.utils;

import com.artillexstudios.axapi.utils.file.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class HistoryUtils {
    private static final DateTimeFormatter FILE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final Path logsDir;

    static {
        logsDir = FileUtils.getInstance().getFolder().toPath().resolve("logs");
        try {
            Files.createDirectories(logsDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void writeToHistory(String text) {
        ZonedDateTime now = ZonedDateTime.now();
        Path logFile = logsDir.resolve("%s.log".formatted(now.format(FILE_FORMAT)));
        String line = "[%s] %s%s".formatted(now.format(TIME_FORMAT), text, System.lineSeparator());
        try {
            Files.writeString(logFile, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
