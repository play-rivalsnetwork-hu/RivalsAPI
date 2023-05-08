package hu.rivalsnetwork.rivalsapi.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class GuiClickListener implements Listener {

    @EventHandler
    public void onInventoryClickEvent(@NotNull final InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof Menu menu)) return;

        final GuiItem guiItem = menu.getItem(event.getSlot());
        if (guiItem == null || guiItem.event() == null) return;
        menu.getItem(event.getSlot()).accept(event);
    }
}
