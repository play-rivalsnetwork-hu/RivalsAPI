package hu.rivalsnetwork.rivalsapi.hologram;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class AbstractHologram implements Hologram {
    private final ObjectArrayList<HologramLine> lines = new ObjectArrayList<>();
    private final double lineHeight;
    private final String id;
    private Location location;

    public AbstractHologram(Location location, String id, double lineHeight) {
        this.id = id;
        this.lineHeight = lineHeight;
        this.location = location;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int size() {
        return lines.size();
    }

    private Location getLocationRel(int lineNum) {
        return location.clone().add(0, -lineHeight * lineNum, 0);
    }

    @Override
    public void addLine(Component line) {
        Location location = getLocationRel(size());
        HologramLine hologramLine = addLine(location);
        hologramLine.setText(line);
        lines.add(hologramLine);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            show(onlinePlayer);
        }
    }

    @Override
    public void setLine(int lineNum, Component text) {
        HologramLine line = lines.get(lineNum);
        if (line == null) return;
        line.setText(text);
    }

    @Override
    public Component getLine(int lineNum) {
        return lines.get(lineNum).getText();
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void show(Player player) {
        for (HologramLine line : lines) {
            line.show(player);
        }
    }

    @Override
    public void hide(Player player) {
        for (HologramLine line : lines) {
            line.hide(player);
        }
    }

    @Override
    public void teleport(Location location) {
        this.location = location;

        for (int i = 0; i < size(); i++) {
            HologramLine line = lines.get(i);
            line.teleport(getLocationRel(i));
        }
    }

    @Override
    public void remove() {
        for (HologramLine line : this.lines) {
            line.remove();
        }
    }

    @Override
    public void removeLine(int lineNum) {
        HologramLine line = this.lines.remove(lineNum);
        line.remove();
        teleport(location);
    }

    public abstract HologramLine addLine(Location location);
}
