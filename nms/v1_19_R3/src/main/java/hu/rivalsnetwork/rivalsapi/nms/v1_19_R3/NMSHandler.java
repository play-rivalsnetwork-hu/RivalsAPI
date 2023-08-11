package hu.rivalsnetwork.rivalsapi.nms.v1_19_R3;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import hu.rivalsnetwork.rivalsapi.hologram.Hologram;
import hu.rivalsnetwork.rivalsapi.items.ItemStack;
import hu.rivalsnetwork.rivalsapi.nms.PacketEntity;
import hu.rivalsnetwork.rivalsapi.schematic.Schematic;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Field;
import java.util.UUID;

public class NMSHandler implements hu.rivalsnetwork.rivalsapi.nms.NMSHandler {

    @Override
    public PacketEntity createPacketEntity(String name, Location location, String texture, String signature) {
        return new hu.rivalsnetwork.rivalsapi.nms.v1_19_R3.PacketEntity(name, location, texture, signature);
    }

    @Override
    public ItemStack wrapItemStack(org.bukkit.inventory.ItemStack itemStack) {
        return new hu.rivalsnetwork.rivalsapi.nms.v1_19_R3.ItemStack(itemStack);
    }

    @Override
    public ItemStack createItemStack(Material material) {
        return new hu.rivalsnetwork.rivalsapi.nms.v1_19_R3.ItemStack(new org.bukkit.inventory.ItemStack(material));
    }

    @Override
    public void setSkullTexture(@NotNull org.bukkit.inventory.ItemStack item, @NotNull String texture) {
        if (!(item.getItemMeta() instanceof SkullMeta meta)) return;
        GameProfile profile = new GameProfile(UUID.randomUUID(), "skulltexture");
        profile.getProperties().put("texture", new Property("textures", texture));
        try {
            Field profileF = meta.getClass().getDeclaredField("profile");
            profileF.setAccessible(true);
            profileF.set(meta, profile);
        } catch (Exception ignored) {
        }

        item.setItemMeta(meta);
    }

    @Override
    public Schematic getSchematic(@NotNull File file) {
        return new hu.rivalsnetwork.rivalsapi.nms.v1_19_R3.Schematic(file);
    }

    @Override
    public Hologram createHologram(Location location, String id, double lineHeight) {
        return new hu.rivalsnetwork.rivalsapi.nms.v1_19_R3.Hologram(location, id, lineHeight);
    }
}
