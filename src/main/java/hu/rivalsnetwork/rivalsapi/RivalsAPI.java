package hu.rivalsnetwork.rivalsapi;

import hu.rivalsnetwork.rivalsapi.users.User;
import hu.rivalsnetwork.rivalsapi.users.Users;
import org.bukkit.entity.Player;

import java.util.UUID;

class RivalsAPI {
    private final RivalsAPIPlugin plugin;

    public RivalsAPI(RivalsAPIPlugin plugin) {
        this.plugin = plugin;
    }

    public static User getUser(UUID uuid) {
        return Users.getUser(uuid);
    }

    public static User getUser(Player player) {
        return Users.getUser(player);
    }
}