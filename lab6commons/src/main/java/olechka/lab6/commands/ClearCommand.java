package olechka.lab6.commands;

import olechka.lab6.State;
import olechka.lab6.interaction.Console;

@CommandDescription("очистить коллекцию")
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
