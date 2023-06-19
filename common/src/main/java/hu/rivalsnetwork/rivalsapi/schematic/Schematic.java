package hu.rivalsnetwork.rivalsapi.schematic;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

// Thanks to: https://www.spigotmc.org/threads/how-to-set-blocks-incredibly-fast.476097/
// Thanks to: https://www.spigotmc.org/threads/loading-pasting-schematics-without-worldedit.543692/
public interface Schematic {

    void paste(@NotNull final Location location, boolean setAir);

    void setBlock(@NotNull World world, int x, int y, int z, BlockData blockData);
}
