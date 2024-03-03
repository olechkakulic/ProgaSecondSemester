package olechka.lab5.commands;

import olechka.lab5.State;
import olechka.lab5.interaction.Console;

public class HistoryCommand implements Command {
    @Override
    public Result execute(State state) {
        return Result.success(String.join(", ", state.getCommandManager().getHistory()));
    }

    @Override
    public void parse(Console console) {

    }
}
