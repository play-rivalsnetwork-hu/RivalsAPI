package hu.rivalsnetwork.rivalsapi;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import hu.rivalsnetwork.rivalsapi.config.Config;
import hu.rivalsnetwork.rivalsapi.config.ConfigType;
import hu.rivalsnetwork.rivalsapi.config.Configuration;
import hu.rivalsnetwork.rivalsapi.gui.GuiClickListener;
import hu.rivalsnetwork.rivalsapi.plugin.RivalsPluginImpl;
import hu.rivalsnetwork.rivalsapi.storage.Storage;
import hu.rivalsnetwork.rivalsapi.users.Users;
import org.bukkit.Bukkit;

public final class RivalsAPIPlugin extends RivalsPluginImpl {
    private static RivalsAPI api;
    private static RivalsAPIPlugin instance;
    @Configuration(configType = ConfigType.YAML, name = "config")
    public static Config CONFIG = null;
    @Configuration(configType = ConfigType.YAML, name = "lang")
    public static Config LANG = null;

    public static RivalsAPIPlugin getInstance() {
        return instance;
    }

    @Override
    public void load() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
    }

    @Override
    public void disable() {
        CommandAPI.onDisable();
    }

    @Override
    public void enable() {
        CommandAPI.onEnable();
        instance = this;
        api = new RivalsAPI(this);

        new Storage(this);

        Users.load();
        Bukkit.getPluginManager().registerEvents(new GuiClickListener(), this);
    }

//    public static Config getConfiguration() {
//        return config;
//    }
//
//    public static Config lang() {
//        return lang;
//    }

    public static RivalsAPI getApi() {
        return api;
    }
}