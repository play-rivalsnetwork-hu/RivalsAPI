package hu.rivalsnetwork.rivalsapi;

import hu.rivalsnetwork.rivalsapi.config.Config;
import hu.rivalsnetwork.rivalsapi.config.ConfigYML;
import hu.rivalsnetwork.rivalsapi.storage.Storage;
import hu.rivalsnetwork.rivalsapi.users.Users;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class RivalsAPIPlugin extends JavaPlugin {
    private static RivalsAPIPlugin instance;
    private static RivalsAPI api;
    private static Config config;

    @Override
    public void onEnable() {
        instance = this;
        api = new RivalsAPI(this);

        config = new ConfigYML();
        new Storage(this);

        Users.load();
    }

    public static @NotNull RivalsAPI getApi() {
        return api;
    }

    public static RivalsAPIPlugin getInstance() {
        return instance;
    }

    public Config getConfiguration() {
        return config;
    }
}