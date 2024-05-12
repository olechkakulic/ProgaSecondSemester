package olechka.lab7.commands;

import olechka.lab7.State;
import olechka.lab7.interaction.Console;

@CommandDescription("очистить коллекцию")
public class ClearCommand implements Command {

    @Override
    public Result execute(State state) {
        if (state.clear()) {
            return Result.success("Удалены ваши элементы коллекции");
        } else {
            return Result.error("В коллекции не было ваших элементов.");
        }
    }

    @Override
    public void parse(Console console) {

    }
}
