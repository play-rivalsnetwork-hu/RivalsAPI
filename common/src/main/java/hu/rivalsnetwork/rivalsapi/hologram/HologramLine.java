package hu.rivalsnetwork.rivalsapi.hologram;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface HologramLine {

    void setText(@NotNull Component text);

    Component getText();

    void hide(Player player);

    void show(Player player);

    void teleport(Location location);

    void remove();
}
