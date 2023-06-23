package hu.rivalsnetwork.rivalsapi.nms.v1_20_R1;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.block.data.CraftBlockData;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

// Thanks to: https://www.spigotmc.org/threads/how-to-set-blocks-incredibly-fast.476097/
// Thanks to: https://www.spigotmc.org/threads/loading-pasting-schematics-without-worldedit.543692/
public class Schematic implements hu.rivalsnetwork.rivalsapi.schematic.Schematic {
    private short width;
    private short height;
    private short length;
    private Map<String, Tag> palette;
    private byte[] blockData;
    final ObjectArraySet<ChunkPos> chunks = new ObjectArraySet<>(10000);

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

    @Override
    public void paste(@NotNull Location location, boolean setAir) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(0, 0, 0);
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
                    pos.set(mutatedX, mutatedY, mutatedZ);

                    for (String data : palette.keySet()) {
                        int i = ((IntTag) palette.get(data)).getAsInt();

                        if (blockData[index] == i) {
                            BlockData bData = Bukkit.createBlockData(data);
                            if (bData.getMaterial().isAir()) continue;
                            setBlock(location.getWorld(), pos, bData);
                        }
                    }
                }
            }
        }

        CraftWorld craftWorld = (CraftWorld) location.getWorld();
        for (ChunkPos chunk : chunks) {
            sendUpdatePackets(craftWorld, chunk);
        }
    }

    @Override
    public void setBlock(@NotNull World world, int x, int y, int z, BlockData blockData) {
        setBlock(world, new BlockPos(x, y, z), blockData);
    }

    public void setBlock(@NotNull World world, @NotNull BlockPos pos, BlockData blockData) {
        CraftWorld craftWorld = ((CraftWorld) world);
        LevelChunk chunk = craftWorld.getHandle().getChunk(pos.getX() >> 4, pos.getZ() >> 4);
        BlockState blockState = ((CraftBlockData) blockData).getState();
        int i = pos.getY();
        LevelChunkSection section = chunk.getSection(chunk.getSectionIndex(i));
        boolean flag1 = section.hasOnlyAir();
        if (flag1 && blockState.isAir()) {
            return;
        }

        chunks.add(chunk.getPos());
        int j = pos.getX() & 15;
        int k = i & 15;
        int l = pos.getZ() & 15;

        section.setBlockState(j, k, l, blockState);
    }

    private void sendUpdatePackets(@NotNull CraftWorld world, @NotNull ChunkPos chunkPos) {
        sendUpdatePacket(world.getHandle().getChunk(chunkPos.x, chunkPos.z));
    }

    // Craftbukkit code start
    private void sendUpdatePacket(@NotNull LevelChunk chunk) {
        ChunkHolder playerChunk = chunk.level.getChunkSource().chunkMap.getVisibleChunkIfPresent(ChunkPos.asLong(chunk.locX, chunk.locZ));
        if (playerChunk == null) return;
        List<ServerPlayer> playersInRange = playerChunk.playerProvider.getPlayers(playerChunk.getPos(), false);

        ClientboundLevelChunkWithLightPacket lightPacket = new ClientboundLevelChunkWithLightPacket(chunk, chunk.level.getLightEngine(), null, null, false);
        int size = playersInRange.size();
        for (int i = 0; i < size; i++) {
            ServerPlayer player = playersInRange.get(i);
            player.connection.send(lightPacket);
        }
    }
    // Craftbukkit code end
}
