package hu.rivalsnetwork.rivalsapi.commands.rivalsapi;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.LiteralArgument;
import hu.rivalsnetwork.rivalsapi.RivalsAPIPlugin;
import hu.rivalsnetwork.rivalsapi.commands.Command;
import hu.rivalsnetwork.rivalsapi.items.ItemStack;
import hu.rivalsnetwork.rivalsapi.nms.NMSHandler;
import hu.rivalsnetwork.rivalsapi.nms.NMSHandlers;
import hu.rivalsnetwork.rivalsapi.users.User;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class RivalsAPICommand{

    @Command
    public static void register() {
        new CommandTree("rivalsapi")
                .withPermission("rivalsapi.command.main")
                .then(new LiteralArgument("reload")
                        .withPermission("rivalsapi.command.reload")
                        .executes((sender, args) -> {
                            long start = System.currentTimeMillis();
                            RivalsAPIPlugin.LANG.reload();
                            RivalsAPIPlugin.CONFIG.reload();
                            RivalsAPIPlugin.getApi().messageUtils().sendLang(sender, "reload", Map.of("%time%", String.valueOf(System.currentTimeMillis() - start)));
                        })
                )
                .then(new LiteralArgument("version")
                        .executesPlayer(info -> {
                            User user = RivalsAPIPlugin.getApi().getUser(info.sender());
                            RivalsAPIPlugin.getApi().messageUtils().sendLang(info.sender(), "version", Map.of("%version%", user.getVersion().name()));
                        })
                )
                .then(new LiteralArgument("itemstack")
                        .withPermission("rivalsapi.command.itemstack")
                        .executesPlayer(info -> {
                            ItemStack item = NMSHandlers.getHandler().wrapItemStack(new org.bukkit.inventory.ItemStack(Material.STICK));
                            item.setAmount(10);
                            item.name(MiniMessage.miniMessage().deserialize("<red>Oreo"));
                            item.lore(List.of(MiniMessage.miniMessage().deserialize("<red>Oreo"), MiniMessage.miniMessage().deserialize("<white>oreo2")));

                            info.sender().getInventory().addItem(item.asBukkitStack());
                        })
                )
                .register();
    }
}
