package hu.rivalsnetwork.rivalsapi.hologram;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface Hologram {

    String getId();

    int size();

    void addLine(Component line);

    void setLine(int lineNum, Component text);

    Component getLine(int lineNum);

    Location getLocation();

    void teleport(Location location);

    void show(Player player);

    void hide(Player player);

    void remove();

    void removeLine(int lineNum);
}
