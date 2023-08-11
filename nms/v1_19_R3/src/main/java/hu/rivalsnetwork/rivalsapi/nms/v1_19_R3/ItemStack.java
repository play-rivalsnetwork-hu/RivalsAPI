package hu.rivalsnetwork.rivalsapi.nms.v1_19_R3;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftNamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ItemStack implements hu.rivalsnetwork.rivalsapi.items.ItemStack {
    private static final String DISPLAY_TAG = "display";
    private final net.minecraft.world.item.ItemStack parent;
    private final org.bukkit.inventory.ItemStack itemStack;

    public ItemStack(org.bukkit.inventory.ItemStack itemStack) {
        this.itemStack = itemStack;
        this.parent = CraftItemStack.asNMSCopy(itemStack);
    }

    @Override
    public void name(Component name) {
        setDisplayTag(parent.getOrCreateTag(), "Name", StringTag.valueOf(net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson().serialize(name)));
    }

    @Override
    public Component name() {
        CompoundTag parentTag = parent.getTag();
        if (parentTag == null) return Component.empty();
        if (parentTag.contains(DISPLAY_TAG)) {
            CompoundTag display = parentTag.getCompound(DISPLAY_TAG);

            if (display.contains("Name")) {
                return net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson().deserialize(display.getString("name"));
            }
        }

        return Component.empty();
    }

    @Override
    public void lore(List<Component> lore) {
        ListTag tag = new ListTag();
        List<String> jsonLore = PaperAdventure.asJson(lore);

        for (int i = 0; i < jsonLore.size(); i++) {
            tag.add(StringTag.valueOf(jsonLore.get(i)));
        }

        setDisplayTag(parent.getOrCreateTag(), "Lore", tag);
    }

    @Override
    public List<Component> lore() {
        CompoundTag parentTag = parent.getOrCreateTag();

        if (parentTag.contains(DISPLAY_TAG)) {
            CompoundTag display = parentTag.getCompound(DISPLAY_TAG);
            if (!display.contains("Lore")) return Collections.emptyList();

            ListTag list = display.getList("Lore", CraftMagicNumbers.NBT.TAG_STRING);
            ArrayList<String> lore = new ArrayList<>(list.size());
            for (int index = 0; index < list.size(); index++) {
                String line = list.getString(index);
                lore.add(line);
            }

            return PaperAdventure.asAdventureFromJson(lore);
        }
        return Collections.emptyList();
    }

    @Override
    public void setAmount(int amount) {
        parent.setCount(amount);
    }

    @Override
    public int getCustomModelData() {
        return parent.getOrCreateTag().getInt("CustomModelData");
    }

    @Override
    public void setCustomModelData(int data) {
        CompoundTag parentTag = parent.getOrCreateTag();

        if (data == -1) {
            parentTag.remove("CustomModelData");
        } else {
            parentTag.putInt("CustomModelData", data);
        }
    }

    @Override
    public void setType(Material type) {
        if (type == Material.AIR) {
            this.parent.setTag(null);
        }

        this.parent.setItem(BuiltInRegistries.ITEM.getOptional(CraftNamespacedKey.toMinecraft(type.getKey())).orElseThrow());
    }

    @Override
    public int getAmount() {
        return this.parent.getCount();
    }

    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        ListTag tags = this.parent.getEnchantmentTags();
        HashMap<Enchantment, Integer> foundEnchants = new HashMap<>(tags.size());

        for (int i = 0; i < tags.size(); i++) {
            CompoundTag compound = (CompoundTag) tags.get(i);
            String key = compound.getString("id");
            int level = 0xffff & compound.getShort("lvl");
            Enchantment found = Enchantment.getByKey(NamespacedKey.minecraft(key));
            if (found != null) {
                foundEnchants.put(found, level);
            }
        }

        return foundEnchants;
    }

    @Override
    public int getEnchantmentLevel(Enchantment enchantment) {
        ListTag tags = this.parent.getEnchantmentTags();
        String enchantKey = enchantment.getKey().toString();

        for (int i = 0; i < tags.size(); i++) {
            CompoundTag compound = (CompoundTag) tags.get(i);
            String key = compound.getString("id");
            if (!key.equals(enchantKey)) {
                continue;
            }

            return 0xffff & compound.getShort("lvl");
        }
        return 0;
    }

    @Override
    public void addItemFlags(ItemFlag... itemFlags) {
        byte flag = parent.getTag() != null ? parent.getTag().contains("HideFlags", 99) ? (byte) this.parent.getTag().getInt("HideFlags") : 0 : 0;
        for (ItemFlag itemFlag : itemFlags) {
            flag |= (byte) (itemFlag.ordinal() << 1);
        }

        this.parent.getOrCreateTag().putInt("HideFlags", flag);
    }

    @Override
    public void removeItemFlags(ItemFlag... itemFlags) {
        byte flag = parent.getTag() != null ? parent.getTag().contains("HideFlags", 99) ? (byte) this.parent.getTag().getInt("HideFlags") : 0 : 0;
        for (ItemFlag itemFlag : itemFlags) {
            flag &= (byte) ~(byte) (1 << itemFlag.ordinal());
        }

        this.parent.getOrCreateTag().putInt("HideFlags", flag);
    }

    @Override
    public Set<ItemFlag> getItemFlags() {
        Set<ItemFlag> flags = new HashSet<>(ItemFlag.values().length);

        for (ItemFlag flag : ItemFlag.values()) {
            if (!hasItemFlag(flag)) continue;
            flags.add(flag);
        }

        return flags;
    }

    @Override
    public boolean hasItemFlag(ItemFlag itemFlag) {
        byte flag = parent.getTag() != null ? parent.getTag().contains("HideFlags", 99) ? (byte) this.parent.getTag().getInt("HideFlags") : 0 : 0;
        int bitModifier = (byte) (1 << itemFlag.ordinal());
        return (flag & bitModifier) == bitModifier;
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return this.parent.asBukkitMirror().getItemMeta().getPersistentDataContainer();
    }

    @Override
    public org.bukkit.inventory.ItemStack asBukkitStack() {
        return parent.asBukkitMirror();
    }

    private void setDisplayTag(@NotNull CompoundTag tag, String key, @Nullable Tag value) {
        final CompoundTag display = tag.getCompound(DISPLAY_TAG);

        if (!tag.contains(DISPLAY_TAG)) {
            tag.put(DISPLAY_TAG, display);
        }

        display.remove(key);

        if (value != null) {
            display.put(key, value);
        }
    }

    @Override
    public boolean isSimilar(hu.rivalsnetwork.rivalsapi.items.ItemStack item) {
        if (this == item) {
            return true;
        }

        if (item == null) {
            return false;
        }

        if (this.getClass() != item.getClass()) {
            return false;
        }

        ItemStack other = (ItemStack) item;
        return this.hashCode() == other.hashCode();
    }

    @Override
    public void addNBT(String key, Object value) {
        if (value instanceof String str) {
            this.parent.getOrCreateTag().putString(key, str);
        } else if (value instanceof Integer integer) {
            this.parent.getOrCreateTag().putInt(key, integer);
        } else if (value instanceof Double doubleValue) {
            this.parent.getOrCreateTag().putDouble(key, doubleValue);
        } else if (value instanceof CompoundTag tag) {
            this.parent.getOrCreateTag().put(key, tag);
        }
    }

    @Override
    public Object get(String key) {
        return this.parent.getOrCreateTag().get(key);
    }

    @Override
    public boolean hasNBT(String key) {
        return this.parent.getOrCreateTag().contains(key);
    }

    @Override
    public void removeNBT(String key) {
        this.parent.getOrCreateTag().remove(key);
    }

    @Override
    public int hashCode() {
        int hash = 1;

        hash = hash * 31 + CraftMagicNumbers.getMaterial(this.parent.getItem()).hashCode();
        hash = hash * 31 + getAmount();
        hash = hash * 31 + getCustomModelData() & 0xffff;
        hash = hash * 31 + getEnchantments().size();
        hash = hash * 31 + this.name().hashCode();
        hash = hash * 31 + this.lore().size();
        hash = hash * 31 + this.getItemFlags().size();

        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemStack itemStack1)) return false;

        if (!Objects.equals(parent, itemStack1.parent)) return false;
        return Objects.equals(itemStack, itemStack1.itemStack);
    }
}
