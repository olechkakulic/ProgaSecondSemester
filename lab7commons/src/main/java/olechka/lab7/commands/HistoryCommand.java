package olechka.lab7.commands;

import olechka.lab7.State;
import olechka.lab7.interaction.Console;

@CommandDescription("вывести последние 14 команд (без их аргументов)")
public class HistoryCommand implements Command {
    @Override
    public Result execute(State state) {
        return Result.success(String.join(", ", state.getCommandManager().getHistory()));
    }

    @Override
    public void parse(Console console) {

    }
}
