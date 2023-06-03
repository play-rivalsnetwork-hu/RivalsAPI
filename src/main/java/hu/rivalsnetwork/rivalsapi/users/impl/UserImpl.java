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
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

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

    public void write(String key, DataType dataType, Key... keys) {
        if (dataType == DataType.MONGODB) {
            Storage.mongo(database -> {
                MongoCollection<Document> collection = database.getCollection(key);
                Document filterDocument = new Document();
                filterDocument.put("uuid", player.getUniqueId().toString());

                Document document = new Document();
                for (Key data : keys) {
                    document.put(data.namespace(), data.value());
                }

                collection.updateMany(filterDocument, document, new UpdateOptions().upsert(true));
            });
        } else {
            Storage.sql(connection -> {
                try (PreparedStatement statement = connection.prepareStatement(Arrays.stream(keys).findFirst().get().namespace())) {
                    statement.executeUpdate();
                }
            });
        }
    }

    public Object read(Key key, DataType dataType) {
        Object[] result = {null};
        if (dataType == DataType.MONGODB) {
            Storage.mongo(database -> {
                MongoCollection<Document> collection =  database.getCollection(key.namespace());

                Document filter = new Document();
                filter.put("uuid", player.getUniqueId().toString());
                FindIterable<Document> cursor = collection.find(filter);

                try (MongoCursor<Document> find = cursor.cursor()) {
                    if (find.hasNext()) {
                        result[0] = find.next().get((String) key.value());
                    }
                }
            });
        } else {
            Storage.sql(connection -> {
                try (PreparedStatement statement = connection.prepareStatement(key.namespace())) {
                    try (ResultSet resultSet = statement.executeQuery()) {
                        result[0] = resultSet;
                    }
                }
            });
        }

        return result[0];
    }
}
