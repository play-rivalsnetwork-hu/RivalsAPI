package hu.rivalsnetwork.rivalsapi.commands.rivalsapi;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import hu.rivalsnetwork.rivalsapi.RivalsAPIPlugin;
import hu.rivalsnetwork.rivalsapi.commands.Command;
import hu.rivalsnetwork.rivalsapi.nms.NMSHandlers;
import hu.rivalsnetwork.rivalsapi.schematic.Schematic;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class SchematicCommand {

    public static String[] getSchematicFiles() {
        final File[] files = new File(RivalsAPIPlugin.getInstance().getDataFolder(), "/schematics").listFiles();
        ArrayList<String> schematics = new ArrayList<>();

        if (files == null) return schematics.toArray(new String[0]);
        for (File file : files) {
            schematics.add(file.getName());
        }

        return schematics.toArray(new String[0]);
    }

    @Command
    public static void register() {
        final File folder = new File(RivalsAPIPlugin.getInstance().getDataFolder(), "/schematics");
        folder.mkdirs();

        new CommandTree("schematic")
                .withPermission("rivalsisland.schematic")
                .then(new StringArgument("file")
                        .replaceSuggestions(ArgumentSuggestions.stringsAsync(async -> CompletableFuture.supplyAsync(SchematicCommand::getSchematicFiles)))
                        .executesPlayer(info -> {
                            Schematic schematic = NMSHandlers.getHandler().getSchematic(new File(RivalsAPIPlugin.getInstance().getDataFolder(), "schematics/" + info.args().get("file")));
                            schematic.paste(info.sender().getLocation(), false);
                        })
                        .then(new BooleanArgument("air")
                                .executesPlayer(info -> {
                                    Schematic schematic = NMSHandlers.getHandler().getSchematic(new File(RivalsAPIPlugin.getInstance().getDataFolder(), "schematics/" + info.args().get("file")));
                                    schematic.paste(info.sender().getLocation(), (boolean) info.args().get("air"));
                                })
                        )
                )
                .register();
    }
}
