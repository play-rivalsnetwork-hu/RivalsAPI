package hu.rivalsnetwork.rivalsapi.serializer.impl;

import hu.rivalsnetwork.rivalsapi.serializer.Serializer;
import hu.rivalsnetwork.rivalsapi.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class LocationSerializer implements Serializer {

    @Override
    public String serialize(@NotNull Object object) {
        if (!(object instanceof Location location)) return null;

        return StringUtils.formatPlaceholders("{};{};{};{};{};{}", location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @Override
    public Object deserialize(@NotNull String object) {
        String[] split = object.split(";");
        World world = Bukkit.getWorld(split[0]);
        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);
        float yaw = Float.parseFloat(split[4]);
        float pitch = Float.parseFloat(split[5]);

        return new Location(world, x, y, z, yaw, pitch);
    }
}
