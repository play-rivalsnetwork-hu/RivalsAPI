package hu.rivalsnetwork.rivalsapi.users;

import hu.rivalsnetwork.rivalsapi.version.Version;
import org.bukkit.entity.Player;

import java.util.List;

public interface User {
    Version getVersion();

    Player getPlayer();

    void sendMessage(String message);

    void sendActionbar(String message);

    void sendTitle(String title, String subTitle);

    void write(String key, DataType dataType, Key... keys);

    void write(String key, DataType dataType, List<Key> filter, Key... keys);

    Object read(Key key, DataType dataType);

    Object read(Key key, DataType dataType, List<Key> filter);

    void delete(String key, DataType dataType);

    void delete(String key, DataType dataType, List<Key> filter);

    void remove(Key key, DataType dataType);

    void remove(Key key, DataType dataType, List<Key> filter);

    enum DataType {
        MYSQL,
        MONGODB
    }
}
