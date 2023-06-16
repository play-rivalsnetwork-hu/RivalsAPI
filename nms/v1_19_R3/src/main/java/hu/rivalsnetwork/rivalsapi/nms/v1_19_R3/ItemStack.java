package hu.rivalsnetwork.rivalsapi.nms.v1_19_R3;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftMagicNumbers;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public void setPersistentDataKey(String key, PersistentDataType<?, ?> type) {

    }

    @Override
    public org.bukkit.inventory.ItemStack asBukkitStack() {
        return parent.asBukkitMirror();
    }

    private void setDisplayTag(@NotNull CompoundTag tag, String key, Tag value) {
        final CompoundTag display = tag.getCompound(DISPLAY_TAG);

        if (!tag.contains(DISPLAY_TAG)) {
            tag.put(DISPLAY_TAG, display);
        }

        display.remove(key);

        if (value != null) {
            display.put(key, value);
            parent.setTag(tag);
        }
    }
}
