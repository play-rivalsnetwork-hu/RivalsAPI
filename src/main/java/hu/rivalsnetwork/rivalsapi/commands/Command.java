package hu.rivalsnetwork.rivalsapi.commands;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {
    private static final List<Command> registerableCommands = new ArrayList<>();

    public Command() {
        registerableCommands.add(this);
    }

    public abstract void register();

    public static void registerAllCommands() {
        for (Command registerableCommand : registerableCommands) {
            registerableCommand.register();
        }
    }
}
