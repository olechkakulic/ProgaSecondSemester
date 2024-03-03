package olechka.lab5.commands;

import olechka.lab5.State;
import olechka.lab5.interaction.Console;

import java.util.Map;
import java.util.stream.Collectors;

//вывести справку по доступным командам
public class HelpCommand implements Command {
    @Override
    public Result execute(State state) {
        Map<String, String> helpCommand = state.getCommandManager().getHelpMap();
        String help = helpCommand.entrySet().stream()
                .map((entry) -> entry.getKey() + " " + entry.getValue())
                .collect(Collectors.joining("\n"));
        return Result.success(help);
    }

    @Override
    public void parse(Console console) {

    }
}
