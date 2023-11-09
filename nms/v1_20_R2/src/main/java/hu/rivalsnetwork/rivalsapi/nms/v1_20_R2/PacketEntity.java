package hu.rivalsnetwork.rivalsapi.nms.v1_20_R2;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import hu.rivalsnetwork.rivalsapi.users.User;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class PacketEntity implements hu.rivalsnetwork.rivalsapi.nms.PacketEntity {
    private final Packet<?> infoPacket;
    private final Packet<?> spawnPacket;
    private final Packet<?> removePacket;
    private final ServerPlayer player;

    public PacketEntity(String name, Location location, String texture, String signature) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
        gameProfile.getProperties().put("textures", new Property("textures", texture, signature));

        this.player = new ServerPlayer(MinecraftServer.getServer(), ((CraftWorld) location.getWorld()).getHandle().getLevel(), gameProfile, ClientInformation.createDefault());
        this.player.setPos(location.getX(), location.getY(), location.getZ());
        infoPacket = new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, this.player);
        spawnPacket = new ClientboundAddEntityPacket(this.player);
        removePacket = new ClientboundPlayerInfoRemovePacket(List.of(this.player.getUUID()));
    }


    @Override
    public void spawn(@NotNull User user) {
        ServerPlayer player = ((CraftPlayer) user.getPlayer()).getHandle();
        player.connection.send(infoPacket);
        player.connection.send(spawnPacket);
        player.connection.send(removePacket);
    }

    @Override
    public void startTicking(int tickDuration) {

    }

    @Override
    public void onClick(@NotNull ClickAction action) {

    }
}
