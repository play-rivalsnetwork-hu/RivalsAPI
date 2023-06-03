package hu.rivalsnetwork.rivalsapi.plugin;

import hu.rivalsnetwork.rivalsapi.RivalsAPI;
import hu.rivalsnetwork.rivalsapi.RivalsAPIPlugin;
import hu.rivalsnetwork.rivalsapi.commands.Command;
import hu.rivalsnetwork.rivalsapi.config.Configuration;
import hu.rivalsnetwork.rivalsapi.serializer.impl.LocationSerializer;
import hu.rivalsnetwork.rivalsapi.users.User;
import hu.rivalsnetwork.rivalsapi.utils.MessageUtils;
import hu.rivalsnetwork.rivalsapi.utils.RivalsLogger;
import hu.rivalsnetwork.rivalsapi.utils.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public abstract class RivalsPluginImpl extends JavaPlugin implements RivalsPlugin {
    private static Scheduler scheduler;
    private static final LocationSerializer serializer = new LocationSerializer();
    private static RivalsLogger logger;
    private ScheduledThreadPoolExecutor executor = null;

    @Override
    public RivalsAPI getAPI() {
        return RivalsAPIPlugin.getInstance().getAPI();
    }

    // Paper & Bukkit code - start
    public static JavaPlugin getPlugin(@NotNull Class clazz) {
        final ClassLoader cl = clazz.getClassLoader();
        if (!(cl instanceof io.papermc.paper.plugin.provider.classloader.ConfiguredPluginClassLoader configuredPluginClassLoader)) { // Paper
            throw new IllegalArgumentException(clazz + " is not initialized by a " + io.papermc.paper.plugin.provider.classloader.ConfiguredPluginClassLoader.class); // Paper
        }
        JavaPlugin plugin = configuredPluginClassLoader.getPlugin(); // Paper
        if (plugin == null) {
            throw new IllegalStateException("Cannot get plugin for " + clazz + " from a static initializer");
        }

        return (JavaPlugin) clazz.cast(plugin);
    }
    // Paper & Bukkit code - end

    @Override
    public void enable() {

    }

    @Override
    public void load() {

    }

    @Override
    public void onLoad() {
        super.onLoad();
        load();
    }


    @Override
    public void disable() {

    }

    @Override
    public void onDisable() {
        super.onDisable();
        executor.shutdownNow();
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
    public void onEnable() {
        super.onEnable();
        executor = new ScheduledThreadPoolExecutor(3, task -> new Thread(task, "RivalsAPI-Executor-$name".replace("$name", this.getName())));
        scheduler = new Scheduler(this);
        logger = new RivalsLogger(this);
        logger().info("<white>Loading plugin <green>" + this.getName());

        loadConfigs();
        this.enable();
        loadCommands();
    }

    @Override
    public void loadCommands() {
        long now = System.currentTimeMillis();
        logger.info("<green>Loading commands...");
        Reflections reflections = new Reflections(getClassLoader(), new MethodAnnotationsScanner());

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
    public void loadConfigs() {
        long now = System.currentTimeMillis();
        logger().info("<green>Loading configs...");
        Reflections reflections = new Reflections(getClassLoader(), new FieldAnnotationsScanner());

        for (Field field : reflections.getFieldsAnnotatedWith(Configuration.class)) {
            Configuration config = field.getDeclaredAnnotation(Configuration.class);
            try {
                Object obj = getClassObject();
                field.set(obj, new hu.rivalsnetwork.rivalsapi.config.Config(getPlugin(field.getDeclaringClass()), config.name() + config.configType().value) {
                });
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        logger().info("<green>Loaded configs! <gray>Took <green>" + (System.currentTimeMillis() - now) + " <gray>ms!");
    }

    protected @Nullable Object getClassObject() {
        return null;
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

    @Override
    public ScheduledThreadPoolExecutor executor() {
        return executor;
    }
}
