package hu.rivalsnetwork.rivalsapi.users;

import hu.rivalsnetwork.rivalsapi.version.Version;
import org.bukkit.entity.Player;

public interface User {
    Version getVersion();

    Player getPlayer();

    void sendMessage(String message);

    void sendActionbar(String message);

    void sendTitle(String title, String subTitle);

    void write(String key, DataType dataType, Key... keys);

    Object read(Key key, DataType dataType)

    enum DataType {
        MYSQL,
        MONGODB
    }
}
