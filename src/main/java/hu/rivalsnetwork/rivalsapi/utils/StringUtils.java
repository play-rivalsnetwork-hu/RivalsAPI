package hu.rivalsnetwork.rivalsapi.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class StringUtils {
    private static final LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.builder().character('\u00a7').useUnusualXRepeatedCharacterHexFormat().hexColors().build();

    @NotNull
    public static String format(@NotNull String message) {
        message = message.replace('\u00a7', '&');
        message = toLegacy(MiniMessage.miniMessage().deserialize(message));
        message = ChatColor.translateAlternateColorCodes('&', message);

        return message;
    }

    @NotNull
    public static Component formatToComponent(@NotNull String message) {
        return toComponent(format(message));
    }

    public static @NotNull String toLegacy(@NotNull Component component) {
        return LEGACY_COMPONENT_SERIALIZER.serialize(component);
    }

    public static @NotNull Component toComponent(@NotNull String message) {
        return LEGACY_COMPONENT_SERIALIZER.deserialize(message);
    }

    public static String formatPlaceholders(@NotNull String message, Object... args) {
        if (args == null) return message;
        for (Object arg : args) {
            if (arg == null) continue;
            message = org.apache.commons.lang3.StringUtils.replaceOnce(message, "{}", arg.toString());
        }

        return message;
    }

    @NotNull
    public static String formatTime(long time) {
        Duration remainingTime = Duration.ofMillis(time);
        long total = remainingTime.getSeconds();
        long hours = total / 3600;
        long minutes = (total % 3600) / 60;
        long seconds = total % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @NotNull
    public static List<String> formatList(@NotNull List<String> list) {
        List<String> toReturn = new ArrayList<>(list.size());
        for (String line : list) {
            toReturn.add(StringUtils.format(line));
        }

        return toReturn;
    }

    @NotNull
    public static List<Component> formatListToComponent(@NotNull List<String> list) {
        List<Component> toReturn = new ArrayList<>(list.size());
        for (String line : list) {
            toReturn.add(StringUtils.toComponent(StringUtils.format(line)));
        }

        return toReturn;
    }
}