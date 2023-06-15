package hu.rivalsnetwork.rivalsapi.items;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class CustomItem {
    private static int unique = Integer.MIN_VALUE;
    private final ItemStack item;
    private final int hash;

    public CustomItem(@NotNull final ItemStack item) {
        this.item = item;
        CustomItems.customItems().add(this);
        this.hash = next();
    }

    private static int next() {
        if (unique == Integer.MAX_VALUE) {
            unique = Integer.MIN_VALUE;
        } else {
            unique += 1;
        }
        return unique;
    }

    public ItemStack getItem() {
        return this.item;
    }

    @Override
    public int hashCode() {
        return this.hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }

        CustomItem other = (CustomItem) obj;
        return this.hash == other.hash;
    }
}
