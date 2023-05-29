package hu.rivalsnetwork.rivalsapi.utils;

import hu.rivalsnetwork.rivalsapi.plugin.RivalsPluginImpl;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class RivalsLogger {
    private final String name;

    public RivalsLogger(@NotNull RivalsPluginImpl plugin) {
        name = plugin.getName();
    }

    public void warn(String message, Object... args) {
        Bukkit.getConsoleSender().sendMessage(StringUtils.format("[" + name + "] <yellow>WARN</yellow> " + StringUtils.formatPlaceholders(message, args)));
    }

    public void info(String message, Object... args) {
        Bukkit.getConsoleSender().sendMessage(StringUtils.format("[" + name + "] <green>INFO</green> " + StringUtils.formatPlaceholders(message, args)));
    }

    public void severe(String message, Object... args) {
        Bukkit.getConsoleSender().sendMessage(StringUtils.format("[" + name + "] <dark_red>SEVERE</dark_red> " + StringUtils.formatPlaceholders(message, args)));
    }

    public void config(String message, Object... args) {
        Bukkit.getConsoleSender().sendMessage(StringUtils.format("[" + name + "] <gold>CONFIG</gold> " + StringUtils.formatPlaceholders(message, args)));
    }


    public void fine(String message, Object... args) {
        Bukkit.getConsoleSender().sendMessage(StringUtils.format("[" + name + "] <green>FINE</green> " + StringUtils.formatPlaceholders(message, args)));
    }

    public void finer(String message, Object... args) {
        Bukkit.getConsoleSender().sendMessage(StringUtils.format("[" + name + "] <green>FINER</green> " + StringUtils.formatPlaceholders(message, args)));
    }

    public void finest(String message, Object... args) {
        Bukkit.getConsoleSender().sendMessage(StringUtils.format("[" + name + "] <green>FINEST</green> " + StringUtils.formatPlaceholders(message, args)));
    }

    public void all(String message, Object... args) {
        Bukkit.getConsoleSender().sendMessage(StringUtils.format("[" + name + "] <green>ALL</green> " + StringUtils.formatPlaceholders(message, args)));
    }

    public void off(String message, Object... args) {
        Bukkit.getConsoleSender().sendMessage(StringUtils.format("[" + name + "] <green>OFF</green> " + StringUtils.formatPlaceholders(message, args)));
    }
}