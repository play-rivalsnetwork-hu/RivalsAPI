package hu.rivalsnetwork.rivalsapi.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Menu implements InventoryHolder {
    private static final HashMap<Integer, GuiItem> menuItems = new HashMap<>();
    private final Inventory inventory;

    public Menu(@NotNull String title, int size) {
        inventory = Bukkit.createInventory(this, size, title);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public GuiItem getItem(int slot) {
        return menuItems.get(slot);
    }

    public void setItem(int slot, @NotNull GuiItem item) {
        menuItems.put(slot, item);
    }


    public void open(@NotNull Player player) {
        inventory.clear();
        menuItems.forEach((slot, item) -> {
            inventory.setItem(slot, item.getItem());
        });

        player.openInventory(inventory);
    }
}
