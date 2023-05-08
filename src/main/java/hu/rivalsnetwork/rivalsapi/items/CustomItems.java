package hu.rivalsnetwork.rivalsapi.items;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CustomItems {
    private static final List<CustomItem> customItems = new ArrayList<>(1024);

    public static void add(@NotNull final CustomItem item) {
        customItems.add(item);
    }

    public static List<CustomItem> customItems() {
        return customItems;
    }
}
