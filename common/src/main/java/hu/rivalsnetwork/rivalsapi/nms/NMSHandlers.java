package hu.rivalsnetwork.rivalsapi.nms;

import hu.rivalsnetwork.rivalsapi.RivalsAPIPlugin;
import org.bukkit.Bukkit;

public class NMSHandlers {
    private static NMSHandler handler;
    private static String version;

    public static void initialize() {
        if (handler != null) return;
        final String packageName = Bukkit.getServer().getClass().getPackage().getName();
        version = packageName.substring(packageName.lastIndexOf('.') + 1);

        try {
            RivalsAPIPlugin.getInstance().logger().fine("<white>Enabling NMS support for version <green>{}</green>...", version);
            handler = (NMSHandler) Class.forName("hu.rivalsnetwork.rivalsapi.nms." + version + ".NMSHandler").getConstructor().newInstance();
            RivalsAPIPlugin.getInstance().logger().fine("<white>NMS Support successfully enabled!");
        } catch (Exception exception) {
            RivalsAPIPlugin.getInstance().logger().severe("<red>Could not initialize NMS version support! Is your version supported? ({},{})", version, packageName);
        }
    }

    public static String getVersion() {
        return version;
    }

    public static NMSHandler getHandler() {
        if (handler == null) {
            initialize();
        }

        return handler;
    }
}
