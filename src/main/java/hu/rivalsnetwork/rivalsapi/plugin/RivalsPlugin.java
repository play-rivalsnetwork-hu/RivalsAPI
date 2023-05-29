package hu.rivalsnetwork.rivalsapi.plugin;

import hu.rivalsnetwork.rivalsapi.RivalsAPI;
import hu.rivalsnetwork.rivalsapi.serializer.impl.LocationSerializer;
import hu.rivalsnetwork.rivalsapi.users.User;
import hu.rivalsnetwork.rivalsapi.utils.MessageUtils;
import hu.rivalsnetwork.rivalsapi.utils.RivalsLogger;
import hu.rivalsnetwork.rivalsapi.utils.Scheduler;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface RivalsPlugin {

    RivalsAPI getAPI();

    void enable();

    void load();

    void disable();

    void reload();

    void loadCommands();

    void loadConfigs();

    String reloadTime();

    Scheduler scheduler();

    MessageUtils messageUtils();

    LocationSerializer locationSerializer();

    User getUser(UUID uuid);

    User getUser(Player player);

    RivalsLogger logger();
}
