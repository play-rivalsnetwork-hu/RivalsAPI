package hu.rivalsnetwork.rivalsapi.commands.rivalsapi;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.LiteralArgument;
import hu.rivalsnetwork.rivalsapi.RivalsAPIPlugin;
import hu.rivalsnetwork.rivalsapi.commands.Command;

import java.util.Map;

public class RivalsAPICommand extends Command {

    @Override
    public void register() {
        new CommandTree("rivalsapi")
                .withPermission("rivalsapi.command.main")
                .then(new LiteralArgument("reload")
                        .withPermission("rivalsapi.command.reload")
                        .executes((sender, args) -> {
                            long start = System.currentTimeMillis();
                            RivalsAPIPlugin.getInstance().lang().reload();
                            RivalsAPIPlugin.getInstance().getConfiguration().reload();
                            RivalsAPIPlugin.getApi().messageUtils().sendLang(sender, "reloadwithtime", Map.of("%time%", String.valueOf(System.currentTimeMillis() - start)));
                        })
                )
                .register();
    }
}
