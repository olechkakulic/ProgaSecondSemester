package olechka.lab6.commands;

import olechka.lab6.CommandFactory;
import olechka.lab6.State;
import olechka.lab6.interaction.Console;

import java.util.Collection;
import java.util.stream.Collectors;
//вывести справку по доступным командам
@CommandDescription("вывести справку по доступным командам")
public class HelpCommand implements Command {
    @Override
    public Result execute(State state) {
        CommandFactory commandFactory = state.getCommandManager().getCommandFactory();
        Collection<String> commandNames = commandFactory.getCommandNames();
//        делаем с помощью стримов. в стриме теперь лежит список названий команд.
        String help = commandNames.stream()
//                с помощью метода map получаем название команды и описание по имени.
                .map((name) -> name + " " + commandFactory.getCommandDescription(name))
                .collect(Collectors.joining("\n"));
        return Result.success(help);
    }

    @Override
    public void parse(Console console) {

    }
}
