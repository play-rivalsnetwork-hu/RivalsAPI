package hu.rivalsnetwork.rivalsapi.users.impl;

import com.viaversion.viaversion.api.Via;
import hu.rivalsnetwork.rivalsapi.users.User;
import hu.rivalsnetwork.rivalsapi.utils.StringUtils;
import hu.rivalsnetwork.rivalsapi.version.Version;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UserImpl implements User {
    private final Player player;
    private final Version version;

    public UserImpl(@NotNull final Player player) {
        this.player = player;
        version = Version.getVersionByID(Via.getAPI().getPlayerVersion(player.getUniqueId()));
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
}
