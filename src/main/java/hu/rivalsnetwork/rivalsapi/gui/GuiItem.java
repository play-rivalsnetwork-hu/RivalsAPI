package hu.rivalsnetwork.rivalsapi.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GuiItem {
    private final ItemStack item;
    private final ClickEventCallback callback;

    public GuiItem(ItemStack item, ClickEventCallback callback) {
        this.callback = callback;
        this.item = item;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public interface ClickEventCallback {
        void accept(InventoryClickEvent event);
    }

    public void accept(@NotNull InventoryClickEvent event) {
        callback.accept(event);
    }

    public ClickEventCallback event() {
        return this.callback;
    }
}
