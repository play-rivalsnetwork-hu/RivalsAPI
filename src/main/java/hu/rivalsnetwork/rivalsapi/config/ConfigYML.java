package hu.rivalsnetwork.rivalsapi.config;

import hu.rivalsnetwork.rivalsapi.RivalsAPIPlugin;

public class ConfigYML extends Config {

    public ConfigYML() {
        super(RivalsAPIPlugin.getInstance(), "config.yml");
    }
}