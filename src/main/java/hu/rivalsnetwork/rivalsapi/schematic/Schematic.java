package hu.rivalsnetwork.rivalsapi.schematic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

public class Schematic {
    private short width;
    private short height;
    private short length;
    private Map<String, Tag> palette;
    private byte[] blockData;

    public Schematic(@NotNull final File file) {
        try (FileInputStream stream = new FileInputStream(file)) {
            CompoundTag nbt = NbtIo.readCompressed(stream);

            this.width = nbt.getShort("Width");
            this.height = nbt.getShort("Height");
            this.length = nbt.getShort("Length");
            this.palette = nbt.getCompound("Palette").tags;
            this.blockData = nbt.getByteArray("BlockData");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void paste(@NotNull final Location location) {
        Location locToMod = new Location(location.getWorld(), 0, 0, 0);
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < length; z++) {
                    int index = y * width * length + z * width + x;
                    locToMod.set(x + location.getX(), y + location.getY(), z + location.getZ());
                    locToMod.getWorld().getChunkAtAsync(locToMod).thenAccept(loc -> {
                        Block block = locToMod.getBlock();

                        for (String data : palette.keySet()) {
                            int i = ((IntTag) palette.get(data)).getAsInt();

                            if (blockData[index] == i) {
                                block.setBlockData(Bukkit.createBlockData(data), false);
                            }
                        }
                    });
                }
            }
        }
    }
}
