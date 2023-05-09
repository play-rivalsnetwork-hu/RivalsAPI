package hu.rivalsnetwork.rivalsapi.schematic;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_19_R3.block.data.CraftBlockData;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

// Thanks to: https://www.spigotmc.org/threads/loading-pasting-schematics-without-worldedit.543692/
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
        int mutatedX;
        int mutatedY;
        int mutatedZ;
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < length; z++) {
                    int index = y * width * length + z * width + x;
                    mutatedX = x + location.getBlockX();
                    mutatedY = y + location.getBlockY();
                    mutatedZ = z + location.getBlockZ();

                    for (String data : palette.keySet()) {
                        int i = ((IntTag) palette.get(data)).getAsInt();

                        if (blockData[index] == i) {
                            setBlock(location.getWorld(), mutatedX, mutatedY, mutatedZ, Bukkit.createBlockData(data));
                        }
                    }
                }
            }
        }
    }

    public void setBlock(@NotNull World world, int x, int y, int z, BlockData blockData) {
        CraftWorld craftWorld = ((CraftWorld) world);
        craftWorld.getChunkAtAsync(x >> 4, z >> 4).thenAccept(chunk -> {
            CraftBlock block = ((CraftBlock) craftWorld.getBlockAt(x, y, z));
            CraftBlock.setTypeAndData(craftWorld.getHandle(), new BlockPos(x, y, z), block.getNMS(), ((CraftBlockData) blockData).getState(), false);
        });
    }
}
