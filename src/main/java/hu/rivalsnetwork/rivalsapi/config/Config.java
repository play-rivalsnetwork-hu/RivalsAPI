package hu.rivalsnetwork.rivalsapi.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import hu.rivalsnetwork.rivalsapi.RivalsAPIPlugin;
import hu.rivalsnetwork.rivalsapi.utils.RivalsLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class Config {
    private String name;
    private YamlDocument config = null;

    public Config(@NotNull JavaPlugin plugin, String name) {
        InputStream resource = plugin.getResource(name);
        if (resource == null) {
            RivalsAPIPlugin.getApi().logger().severe("Could not find {} in plugin {}''s resources! Please notify the authors of said plugin about this!", this.name, plugin.toString());
            return;
        }

        try {
            this.name = name;
            this.config = YamlDocument.create(plugin.getDataFolder().toPath().resolve(name).toFile(), resource, GeneralSettings.DEFAULT, LoaderSettings.DEFAULT, DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build());
        } catch (IOException exception) {
            RivalsAPIPlugin.getApi().logger().severe("An issue occured while loading config {}", this.name);
        }
    }

    public YamlDocument getHandle() {
        return this.config;
    }

    public void reload() {
        try {
            this.config.reload();
        } catch (IOException exception) {
            RivalsAPIPlugin.getApi().logger().severe("An issue occured while reloading config {}", this.name);
        }
    }

    public void save() {
        try {
            this.config.reload();
        } catch (IOException exception) {
            RivalsAPIPlugin.getApi().logger().severe("An issue occured while saving config {}", this.name);
        }
    }

    public String getString(String path) {
        return getHandle().getString(path);
    }

    public int getInt(String path) {
        return getHandle().getInt(path);
    }

    public long getLong(String path) {
        return getHandle().getLong(path);
    }

    public List<String> getStringList(String path) {
        return getHandle().getStringList(path);
    }

    public Section getSection(String path) {
        return getHandle().getSection(path);
    }
}