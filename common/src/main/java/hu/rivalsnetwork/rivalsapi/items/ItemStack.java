package hu.rivalsnetwork.rivalsapi.items;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ItemStack {

    void name(Component name);

    Component name();

    void lore(List<Component> lore);

    List<Component> lore();

    void setAmount(int amount);

    void setCustomModelData(int data);

    int getCustomModelData();

    void setType(Material type);

    int getAmount();

    Map<Enchantment, Integer> getEnchantments();

    int getEnchantmentLevel(Enchantment enchantment);

    void addItemFlags(ItemFlag... itemFlags);

    void removeItemFlags(ItemFlag... itemFlags);

    Set<ItemFlag> getItemFlags();

    boolean hasItemFlag(ItemFlag itemFlag);

    PersistentDataContainer getPersistentDataContainer();

    org.bukkit.inventory.ItemStack asBukkitStack();

    boolean isSimilar(ItemStack item);

    void addNBT(String key, Object value);

    boolean hasNBT(String key);

    Object get(String key);

    void removeNBT(String key);
}
