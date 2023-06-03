/* (c) 2021 Rosewood Development
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software with minimal restriction, including the rights to use, copy, modify or merge while excluding the rights to publish, (re)distribute, sub-license, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The same distribution rights and limitations above shall similarly apply to any and all source code, and other means that can be used to emulate this work.
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package hu.rivalsnetwork.rivalsapi.storage;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hu.rivalsnetwork.rivalsapi.RivalsAPIPlugin;
import hu.rivalsnetwork.rivalsapi.config.Config;
import hu.rivalsnetwork.rivalsapi.utils.StringUtils;
import org.bson.UuidRepresentation;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;

public class Storage {
    private static final HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;
    private static MongoClient client;

    public Storage(@NotNull final RivalsAPIPlugin plugin) {
        Config configYML = RivalsAPIPlugin.CONFIG;
        if (configYML.getHandle().getBoolean("mysql")) {

            config.setJdbcUrl(StringUtils.formatPlaceholders("jdbc:mysql://{}/{}",
                    configYML.getString("storage.address"),
                    configYML.getString("storage.database")
            ));

            config.setUsername(configYML.getString("storage.username"));
            config.setPassword(configYML.getString("storage.password"));
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            config.setPoolName(plugin.getName());

            config.setMaximumPoolSize(configYML.getInt("storage.pool-settings.maximum-pool-size"));
            config.setMinimumIdle(configYML.getInt("storage.pool-settings.minimum-idle"));
            config.setMaxLifetime(configYML.getInt("storage.pool-settings.maximum-lifetime"));
            config.setKeepaliveTime(configYML.getInt("storage.pool-settings.keepalive-time"));
            config.setConnectionTimeout(configYML.getInt("storage.pool-settings.connection-timeout"));

            dataSource = new HikariDataSource(config);
        }

        if (configYML.getHandle().getBoolean("mongodb")) {
            ServerAddress address = new ServerAddress(configYML.getString("storage.address"), 27017);
            MongoCredential credential = MongoCredential.createCredential(configYML.getString("storage.username"), configYML.getString("storage.database"), configYML.getString("storage.password").toCharArray());
            MongoClientSettings settings = MongoClientSettings.builder()
                    .uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
                    .credential(credential)
                    .applyToClusterSettings(builder -> builder.hosts(Collections.singletonList(address))).build();

            client = MongoClients.create(settings);
        }
    }

    public static void sql(@NotNull Callback callback) {
        if (dataSource == null) {
            throw new RuntimeException("MySQL/MariaDB was not enabled in config, yet it's still being used!");
        }

        try (Connection connection = dataSource.getConnection()) {
            callback.accept(connection);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void mongo(@NotNull MongoCallback callback) {
        if (client == null) {
            throw new RuntimeException("MongoDB was not enabled in config, yet it's still being used!");
        }

        try {
            callback.accept(client.getDatabase(RivalsAPIPlugin.CONFIG.getString("storage.database")));
        } catch (MongoException exception) {
            exception.printStackTrace();
        }
    }

    public interface Callback {
        void accept(Connection connection) throws SQLException;
    }

    public interface MongoCallback {
        void accept(MongoDatabase database) throws MongoException;
    }
}
