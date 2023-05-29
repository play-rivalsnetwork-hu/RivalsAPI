package hu.rivalsnetwork.rivalsapi;

import hu.rivalsnetwork.rivalsapi.serializer.impl.LocationSerializer;
import hu.rivalsnetwork.rivalsapi.users.User;
import hu.rivalsnetwork.rivalsapi.users.Users;
import hu.rivalsnetwork.rivalsapi.utils.MessageUtils;
import hu.rivalsnetwork.rivalsapi.utils.RivalsLogger;
import hu.rivalsnetwork.rivalsapi.utils.Scheduler;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RivalsAPI {
    private final RivalsAPIPlugin plugin;
    private static Scheduler scheduler;
    private static MessageUtils messageUtils;
    private final LocationSerializer locationSerializer = new LocationSerializer();

    public RivalsAPI(RivalsAPIPlugin plugin) {
        this.plugin = plugin;
        scheduler = new Scheduler(plugin);
        messageUtils = new MessageUtils(RivalsAPIPlugin.LANG.getHandle());
    }

    public User getUser(UUID uuid) {
        return Users.getUser(uuid);
    }

    public User getUser(Player player) {
        return Users.getUser(player);
    }

    public LocationSerializer getLocationSerializer() {
        return this.locationSerializer;
    }

    public Scheduler scheduler() {
        return scheduler;
    }

    public MessageUtils messageUtils() {
        return messageUtils;
    }

    public RivalsLogger logger() {
        return new RivalsLogger(plugin);
    }
}