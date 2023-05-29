package hu.rivalsnetwork.rivalsapi.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import hu.rivalsnetwork.rivalsapi.RivalsAPIPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class ItemBuilder {

    @NotNull
    public static ItemStack build(@NotNull final Section section) {
        ItemStack item = new ItemStack(matchMaterial(section.getString("material")));

        section.getOptionalString("name").ifPresent(name -> setName(item, name));
        section.getOptionalString("texture").ifPresent(texture -> setTexture(item, texture));
        section.getOptionalStringList("lore").ifPresent(lore -> setLore(item, lore));
        section.getOptionalInt("amount").ifPresent(amount -> setAmount(item, amount));
        section.getOptionalInt("custom-model-data").ifPresent(data -> setCustomModelData(item, data));
        section.getOptionalStringList("enchants").ifPresent(enchants -> addEnchants(item, createEnchantmentsMap(enchants)));
        section.getOptionalStringList("item-flags").ifPresent(flags -> applyItemFlags(item, getItemFlags(flags)));

        return item;
    }

    // https://www.spigotmc.org/threads/how-to-create-heads-with-custom-base64-texture.352562/
    public static void setTexture(@NotNull ItemStack item, @NotNull String texture) {
        if (!(item.getItemMeta() instanceof SkullMeta meta)) return;
        GameProfile profile = new GameProfile(UUID.randomUUID(), "skulltexture");
        profile.getProperties().put("texture", new Property("textures", texture));
        try {
            Field profileF = meta.getClass().getDeclaredField("profile");
            profileF.setAccessible(true);
            profileF.set(meta, profile);
        } catch (Exception ignored) {}

        item.setItemMeta(meta);
    }

    public static void setName(@NotNull ItemStack item, @NotNull String name) {
        item.editMeta(itemMeta -> itemMeta.displayName(StringUtils.toComponent(StringUtils.format(name))));
    }

    public static void setLore(@NotNull ItemStack item, List<String> lore) {
        item.editMeta(itemMeta -> itemMeta.lore(StringUtils.formatListToComponent(lore)));
    }

    public static void setAmount(@NotNull ItemStack item, int amount) {
        item.setAmount(amount);
    }

    public static void setCustomModelData(@NotNull ItemStack item, int data) {
        item.editMeta(itemMeta -> itemMeta.setCustomModelData(data));
    }

    public static void addEnchants(@NotNull ItemStack item, Map<Enchantment, Integer> enchantments) {
        item.addEnchantments(enchantments);
    }

    @NotNull
    public static Map<Enchantment, Integer> createEnchantmentsMap(@NotNull List<String> enchantments) {
        final Map<Enchantment, Integer> enchantsMap = new HashMap<>(enchantments.size());

        for (String enchantment : enchantments) {
            String[] enchant = enchantment.split(":");
            Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(enchant[0]));
            if (ench == null) continue;
            int level;
            try {
                level = Integer.parseInt(enchant[1]);
            } catch (Exception exception) {
                RivalsAPIPlugin.getApi().logger().config("Not an integer! {}", enchant[0]);
                continue;
            }

            enchantsMap.put(ench, level);
        }

        return enchantsMap;
    }

    public static void applyItemFlags(@NotNull ItemStack item, @NotNull List<ItemFlag> flags) {
        for (ItemFlag flag : flags) {
            item.addItemFlags(flag);
        }
    }

    @NotNull
    public static List<ItemFlag> getItemFlags(@NotNull List<String> flags) {
        final List<ItemFlag> flagList = new ArrayList<>(flags.size());
        for (String flag : flags) {
            ItemFlag itemFlag;
            try {
                itemFlag = ItemFlag.valueOf(flag.toUpperCase(Locale.ENGLISH));
            } catch (Exception exception) {
                RivalsAPIPlugin.getApi().logger().config("Invalid flag name {}!", flag);
                continue;
            }

            flagList.add(itemFlag);
        }

        return flagList;
    }

    @NotNull
    private static Material matchMaterial(@NotNull String material) {
        Material matchedMaterial = Material.matchMaterial(material);
        if (matchedMaterial == null) {
            return Material.BEDROCK;
        }

        return matchedMaterial;
    }
}
