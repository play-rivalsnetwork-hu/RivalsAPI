package hu.rivalsnetwork.rivalsapi.nms.v1_19_R3;

import io.papermc.paper.adventure.PaperAdventure;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class HologramLine implements hu.rivalsnetwork.rivalsapi.hologram.HologramLine {
    private final ObjectArrayList<ServerPlayer> viewers = new ObjectArrayList<>();
    private final ArmorStand armorStand;
    private final ClientboundRemoveEntitiesPacket removePacket;
    private Component text;

    public HologramLine(Location location) {
        ServerLevel level = ((CraftWorld) location.getWorld()).getHandle();
        this.armorStand = new ArmorStand(level, location.getX(), location.getY(), location.getZ());
        this.removePacket = new ClientboundRemoveEntitiesPacket(armorStand.getId());
        this.armorStand.setCustomNameVisible(true);
        this.armorStand.setInvisible(true);
        this.armorStand.setMarker(true);
    }

    @Override
    public void setText(@NotNull Component text) {
        this.text = text;
        this.armorStand.setCustomName(PaperAdventure.asVanilla(text));
        ClientboundSetEntityDataPacket dataPacket = entityDataPacket();
        for (ServerPlayer viewer : viewers) {
            viewer.connection.send(dataPacket);
        }
    }

    @Override
    public Component getText() {
        return text;
    }

    @Override
    public void hide(Player player) {
        hide(((CraftPlayer) player).getHandle());
    }

    private void hide(ServerPlayer serverPlayer) {
        this.viewers.remove(serverPlayer);

        serverPlayer.connection.send(new ClientboundRemoveEntitiesPacket(armorStand.getId()));
    }

    @Override
    public void show(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        serverPlayer.connection.send(addEntityPacket());
        serverPlayer.connection.send(entityDataPacket());

        this.viewers.add(serverPlayer);
    }

    @Override
    public void teleport(Location location) {
        this.armorStand.setPos(location.getX(), location.getY(), location.getZ());
        ClientboundTeleportEntityPacket teleportPacket = teleportEntityPacket();

        for (ServerPlayer viewer : this.viewers) {
            viewer.connection.send(teleportPacket);
        }
    }

    @Override
    public void remove() {
        Iterator<ServerPlayer> viewers = this.viewers.iterator();
        while (viewers.hasNext()) {
            ServerPlayer next = viewers.next();
            viewers.remove();
            next.connection.send(removePacket);
        }

        this.armorStand.remove(Entity.RemovalReason.DISCARDED);
    }

    private ClientboundSetEntityDataPacket entityDataPacket() {
        return new ClientboundSetEntityDataPacket(this.armorStand.getId(), this.armorStand.getEntityData().getNonDefaultValues());
    }

    private ClientboundTeleportEntityPacket teleportEntityPacket() {
        return new ClientboundTeleportEntityPacket(this.armorStand);
    }

    private ClientboundAddEntityPacket addEntityPacket() {
        return new ClientboundAddEntityPacket(this.armorStand);
    }
}
