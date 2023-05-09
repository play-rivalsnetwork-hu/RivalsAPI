package hu.rivalsnetwork.rivalsapi.commands;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {
    public static final List<Command> registerableCommands = new ArrayList<>(256);
    private static final List<String> commands = new ArrayList<>(256);

    public Command(String name) {
        commands.add(name);
    }

    public abstract void register();

    public static void registerAllCommands() {
        for (Command registerableCommand : registerableCommands) {
            registerableCommand.register();
        }
    }
}
