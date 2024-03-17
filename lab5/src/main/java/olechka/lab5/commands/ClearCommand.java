package olechka.lab5.commands;

import olechka.lab5.State;
import olechka.lab5.interaction.Console;

public class ClearCommand implements Command {

    @Override
    public Result execute(State state) {
        state.getCollection().clear();
        return Result.success("Коллекция очищена");
    }

    @Override
    public void parse(Console console) {

    }
}
