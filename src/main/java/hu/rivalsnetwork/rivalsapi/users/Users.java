package hu.rivalsnetwork.rivalsapi.users;

import hu.rivalsnetwork.rivalsapi.RivalsAPIPlugin;
import hu.rivalsnetwork.rivalsapi.users.impl.UserImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

public class Users {
    private static final HashMap<UUID, User> USERS = new HashMap<>();

    public static void addUser(@NotNull User user) {
        USERS.put(user.getPlayer().getUniqueId(), user);
    }

    @Nullable
    public static User getUser(@NotNull UUID uuid) {
        return USERS.get(uuid);
    }

    @Nullable
    public static User getUser(@NotNull Player player) {
        return USERS.get(player.getUniqueId());
    }

    public static void removeUser(@NotNull UUID uuid) {
        USERS.remove(uuid);
    }

    public static void removeUser(@NotNull Player player) {
        USERS.remove(player.getUniqueId());
    }

    public static void load() {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerJoinEvent(@NotNull final PlayerJoinEvent event) {
                addUser(new UserImpl(event.getPlayer()));
            }

            @EventHandler
            public void onPlayerQuitEvent(@NotNull final PlayerQuitEvent event) {
                removeUser(event.getPlayer());
            }
        }, RivalsAPIPlugin.getInstance());
    }
}
