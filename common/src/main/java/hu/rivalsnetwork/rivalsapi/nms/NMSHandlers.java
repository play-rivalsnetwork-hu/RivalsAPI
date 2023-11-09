package hu.rivalsnetwork.rivalsapi.nms;

import hu.rivalsnetwork.rivalsapi.RivalsAPIPlugin;
import hu.rivalsnetwork.rivalsapi.version.Version;
import org.bukkit.Bukkit;

public class NMSHandlers {
    private static NMSHandler handler;
    private static Version version;

    public static void initialize() {
        if (handler != null) return;
        version = Version.getVersionByID(Bukkit.getUnsafe().getProtocolVersion());

        try {
            RivalsAPIPlugin.getInstance().logger().fine("<white>Enabling NMS support for version <green>{}</green>...", version.packageName);
            handler = (NMSHandler) Class.forName("hu.rivalsnetwork.rivalsapi.nms." + version.packageName + ".NMSHandler").getConstructor().newInstance();
            RivalsAPIPlugin.getInstance().logger().fine("<white>NMS Support successfully enabled!");
        } catch (Exception exception) {
            RivalsAPIPlugin.getInstance().logger().severe("<red>Could not initialize NMS version support! Is your version supported? ({},{})", version, version.packageName);
        }
    }

    public static String getVersion() {
        return version.toString();
    }

    public static NMSHandler getHandler() {
        if (handler == null) {
            initialize();
        }

        return handler;
    }
}
