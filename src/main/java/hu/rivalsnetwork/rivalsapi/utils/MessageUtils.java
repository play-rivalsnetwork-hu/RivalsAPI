package hu.rivalsnetwork.rivalsapi.utils;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MessageUtils {
    private final YamlDocument file;

    public MessageUtils(@NotNull YamlDocument file) {
        this.file = file;
    }

    public void sendMessage(@NotNull Type type, @NotNull Player player, @NotNull String message) {
        String[] titleSplit = message.split(";");
        switch (type) {
            case CHAT -> player.sendMessage(StringUtils.format(message));
            case ACTIONBAR -> player.sendActionBar(StringUtils.format(message));
            case TITLE -> player.showTitle(Title.title(StringUtils.formatToComponent(titleSplit[0]), StringUtils.formatToComponent(titleSplit[1])));
        }
    }

    public void sendMessage(@NotNull Player player, @NotNull String message) {
        player.sendMessage(StringUtils.format(message));
    }

    public void sendLang(@NotNull Type type, @NotNull Player player, @NotNull String key) {
        String message = file.getString(key);
        String[] titleSplit = message.split(";");
        switch (type) {
            case CHAT -> player.sendMessage(StringUtils.format(message));
            case ACTIONBAR -> player.sendActionBar(StringUtils.format(message));
            case TITLE -> player.showTitle(Title.title(StringUtils.formatToComponent(titleSplit[0]), StringUtils.formatToComponent(titleSplit[1])));
        }
    }

    public  void sendLang(@NotNull Player player, @NotNull String key) {
        player.sendMessage(StringUtils.format(file.getString(key)));
    }

    enum Type {
     CHAT,
     ACTIONBAR,
     TITLE
    }
}
