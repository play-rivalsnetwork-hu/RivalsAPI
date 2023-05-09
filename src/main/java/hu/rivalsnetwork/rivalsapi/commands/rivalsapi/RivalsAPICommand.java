package hu.rivalsnetwork.rivalsapi.commands.rivalsapi;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.LiteralArgument;
import hu.rivalsnetwork.rivalsapi.RivalsAPIPlugin;
import hu.rivalsnetwork.rivalsapi.commands.Command;
import hu.rivalsnetwork.rivalsapi.users.User;

import java.util.Map;

public class RivalsAPICommand extends Command {

    public RivalsAPICommand() {
        super("rivalsapi");
        registerableCommands.add(this);
    }

    @Override
    public void register() {
        new CommandTree("rivalsapi")
                .withPermission("rivalsapi.command.main")
                .then(new LiteralArgument("reload")
                        .withPermission("rivalsapi.command.reload")
                        .executes((sender, args) -> {
                            long start = System.currentTimeMillis();
                            RivalsAPIPlugin.getInstance().lang().reload();
                            RivalsAPIPlugin.getConfiguration().reload();
                            RivalsAPIPlugin.getApi().messageUtils().sendLang(sender, "reload", Map.of("%time%", String.valueOf(System.currentTimeMillis() - start)));
                        })
                )
                .then(new LiteralArgument("version")
                        .executesPlayer(info -> {
                            User user = RivalsAPIPlugin.getApi().getUser(info.sender());
                            RivalsAPIPlugin.getApi().messageUtils().sendLang(info.sender(), "version", Map.of("%version%", user.getVersion().name()));
                        })
                )
                .register();
    }
}
