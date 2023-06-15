package hu.rivalsnetwork.rivalsapi.config;

public enum ConfigType {
    YAML(".yml");

    public final String value;

    ConfigType(String value) {
        this.value = value;
    }
}
