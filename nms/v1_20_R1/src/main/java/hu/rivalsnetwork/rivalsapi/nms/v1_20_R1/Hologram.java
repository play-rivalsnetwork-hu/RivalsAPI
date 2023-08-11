package hu.rivalsnetwork.rivalsapi.nms.v1_20_R1;

import hu.rivalsnetwork.rivalsapi.hologram.AbstractHologram;
import org.bukkit.Location;

public class Hologram extends AbstractHologram {

    public Hologram(Location location, String id, double lineHeight) {
        super(location, id, lineHeight);
    }

    @Override
    public HologramLine addLine(Location location) {
        return new HologramLine(location);
    }
}
