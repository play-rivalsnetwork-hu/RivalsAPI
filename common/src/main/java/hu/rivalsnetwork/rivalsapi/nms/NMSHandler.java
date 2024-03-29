package hu.rivalsnetwork.rivalsapi.nms;

import hu.rivalsnetwork.rivalsapi.hologram.Hologram;
import hu.rivalsnetwork.rivalsapi.items.ItemStack;
import hu.rivalsnetwork.rivalsapi.schematic.Schematic;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface NMSHandler {

    PacketEntity createPacketEntity(String name, Location location, String texture, String signature);

    ItemStack wrapItemStack(org.bukkit.inventory.ItemStack itemStack);

    ItemStack createItemStack(Material material);

    void setSkullTexture(@NotNull org.bukkit.inventory.ItemStack item, @NotNull String texture);

    Schematic getSchematic(@NotNull final File file);

    Hologram createHologram(Location location, String id, double lineHeight);
}
