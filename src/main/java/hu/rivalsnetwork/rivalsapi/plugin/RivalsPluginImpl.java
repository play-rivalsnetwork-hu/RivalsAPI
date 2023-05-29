package hu.rivalsnetwork.rivalsapi.plugin;

import hu.rivalsnetwork.rivalsapi.RivalsAPI;
import hu.rivalsnetwork.rivalsapi.RivalsAPIPlugin;
import hu.rivalsnetwork.rivalsapi.commands.Command;
import hu.rivalsnetwork.rivalsapi.serializer.impl.LocationSerializer;
import hu.rivalsnetwork.rivalsapi.users.User;
import hu.rivalsnetwork.rivalsapi.utils.MessageUtils;
import hu.rivalsnetwork.rivalsapi.utils.RivalsLogger;
import hu.rivalsnetwork.rivalsapi.utils.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public abstract class RivalsPluginImpl extends JavaPlugin implements RivalsPlugin {
    private static Scheduler scheduler;
    private static final LocationSerializer serializer = new LocationSerializer();
    private static RivalsLogger logger;

    @Override
    public RivalsAPI getAPI() {
        return RivalsAPIPlugin.getInstance().getAPI();
    }

    @Override
    public void onEnable() {
        enable();
    }

    @Override
    public void enable() {
        logger().info("<white>Loading plugin <green>" + this.getName());
        scheduler = new Scheduler(this);
        logger = new RivalsLogger(this);;

        scheduler().runLater(this::loadCommands, 10L);
    }

    @Override
    public void load() {
        super.onLoad();
    }

    @Override
    public void onLoad() {
        load();
    }


    @Override
    public void disable() {
        super.onDisable();
    }

    @Override
    public void onDisable() {
        disable();
    }

    @Override
    public void reload() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    @Override
    public Scheduler scheduler() {
        return scheduler;
    }

    @Override
    public void loadCommands() {
        Reflections reflections = new Reflections(getClassLoader(), new MethodAnnotationsScanner());
        long now = System.currentTimeMillis();
        logger().info("<green>Loading commands...");

        for (Method method : reflections.getMethodsAnnotatedWith(Command.class)) {
            try {
                method.invoke(null);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        logger().info("<green>Loaded commands! <gray>Took <green>" + (System.currentTimeMillis() - now) + " <gray>ms!");
    }

    @Override
    public User getUser(UUID uuid) {
        return RivalsAPIPlugin.getApi().getUser(uuid);
    }

    @Override
    public User getUser(Player player) {
        return RivalsAPIPlugin.getApi().getUser(player);
    }

    @Override
    public MessageUtils messageUtils() {
        return null;
    }

    @Override
    public LocationSerializer locationSerializer() {
        return serializer;
    }

    @Override
    public RivalsLogger logger() {
        return logger;
    }

    @Override
    public String reloadTime() {
        long now = System.currentTimeMillis();

        this.reload();

        return String.valueOf(System.currentTimeMillis() - now);
    }
}
