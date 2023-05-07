package hu.rivalsnetwork.rivalsapi.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RivalsLogger {
    private static final Logger logger = Logger.getLogger("RivalsAPI");

    public static void warn(String message, Object... args) {
        logger.warning(StringUtils.formatPlaceholders(message, args));
    }

    public static void info(String message, Object... args) {
        logger.info(StringUtils.formatPlaceholders(message, args));
    }

    public static void severe(String message, Object... args) {
        logger.severe(StringUtils.formatPlaceholders(message, args));
    }

    public static void config(String message, Object... args) {
        logger.config(StringUtils.formatPlaceholders(message, args));
    }

    public static void fine(String message, Object... args) {
        logger.fine(StringUtils.formatPlaceholders(message, args));
    }

    public static void finer(String message, Object... args) {
        logger.finer(StringUtils.formatPlaceholders(message, args));
    }

    public static void finest(String message, Object... args) {
        logger.finest(StringUtils.formatPlaceholders(message, args));
    }

    public static void all(String message, Object... args) {
        logger.log(Level.ALL, StringUtils.formatPlaceholders(message, args));
    }

    public static void off(String message, Object... args) {
        logger.log(Level.OFF, StringUtils.formatPlaceholders(message, args));
    }
}