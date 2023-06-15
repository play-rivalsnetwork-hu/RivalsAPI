package hu.rivalsnetwork.rivalsapi.items;

import net.kyori.adventure.text.Component;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public interface ItemStack {

    void name(Component name);

    Component name();

    void lore(List<Component> lore);

    List<Component> lore();

    void setAmount(int amount);

    void setPersistentDataKey(String key, PersistentDataType<?, ?> type);

    org.bukkit.inventory.ItemStack asBukkitStack();
}
