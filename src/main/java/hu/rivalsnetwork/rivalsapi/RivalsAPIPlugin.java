package hu.rivalsnetwork.rivalsapi;

import hu.rivalsnetwork.rivalsapi.config.Config;
import hu.rivalsnetwork.rivalsapi.config.ConfigYML;
import hu.rivalsnetwork.rivalsapi.config.LangYML;
import hu.rivalsnetwork.rivalsapi.gui.GuiClickListener;
import hu.rivalsnetwork.rivalsapi.storage.Storage;
import hu.rivalsnetwork.rivalsapi.users.Users;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class RivalsAPIPlugin extends JavaPlugin {
    private static RivalsAPIPlugin instance;
    private static RivalsAPI api;
    private static Config config;
    private static Config lang;

    public static @NotNull RivalsAPI getApi() {
        return api;
    }

    public static RivalsAPIPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        api = new RivalsAPI(this);

        config = new ConfigYML();
        lang = new LangYML();
        new Storage(this);

        Users.load();

        Bukkit.getPluginManager().registerEvents(new GuiClickListener(), this);
    }

    public Config getConfiguration() {
        return config;
    }

    public Config lang() {
        return lang;
    }
}