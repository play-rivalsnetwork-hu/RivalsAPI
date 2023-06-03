package hu.rivalsnetwork.rivalsapi.users.impl;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import hu.rivalsnetwork.rivalsapi.storage.Storage;
import hu.rivalsnetwork.rivalsapi.users.Key;
import hu.rivalsnetwork.rivalsapi.users.User;
import hu.rivalsnetwork.rivalsapi.utils.StringUtils;
import hu.rivalsnetwork.rivalsapi.version.Version;
import net.kyori.adventure.title.Title;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserImpl implements User {
    private final Player player;
    private final Version version;

    public UserImpl(@NotNull final Player player) {
        this.player = player;
        version = Version.getVersionByID(player.getProtocolVersion());
    }

    @Override
    public Version getVersion() {
        return this.version;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(StringUtils.format(message));
    }


    @Override
    public void sendActionbar(String message) {
        player.sendActionBar(StringUtils.format(message));
    }

    @Override
    public void sendTitle(String title, String subTitle) {
        Title titleImpl = Title.title(StringUtils.toComponent(StringUtils.format(title)), StringUtils.toComponent(StringUtils.format(subTitle)));
        player.showTitle(titleImpl);
    }

    @Override
    public void write(Key key, Object filter, Object data, DataType dataType) {
        if (dataType == DataType.MONGODB) {
            Storage.mongo(database -> {
                MongoCollection<Document> collection =  database.getCollection(key.namespace());

                Document filter2 = new Document();
                filter2.put(key.namespace(), filter);
                collection.updateMany(filter2, (Bson) data, new UpdateOptions().upsert(true));
            });
        } else {
            Storage.sql(connection -> {
                try (PreparedStatement statement = connection.prepareStatement(((String) data).replace("$key", key.namespace()).replace("$value", key.value()))) {
                    statement.executeUpdate();
                }
            });
        }
    }

    public Object read(Key key, Object filter, DataType dataType) {
        Object[] result = {null};
        if (dataType == DataType.MONGODB) {
            Storage.mongo(database -> {
                MongoCollection<Document> collection =  database.getCollection(key.namespace());

                FindIterable<Document> cursor = collection.find((Bson) filter);
                try (MongoCursor<Document> find = cursor.cursor()) {
                    if (find.hasNext()) {
                        result[0] = find.next().get(key.value());
                    }
                }
            });
        } else {
            Storage.sql(connection -> {
                try (PreparedStatement statement = connection.prepareStatement((String) filter)) {
                    try (ResultSet resultSet = statement.executeQuery()) {
                        result[0] = resultSet;
                    }
                }
            });
        }

        return result[0];
    }
}
