package olechka.lab7.commands;

import olechka.lab7.State;
import olechka.lab7.interaction.Console;

@CommandDescription("вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)")
public class InfoCommand implements Command {

    @Override
    public Result execute(State state) {
//        к строке можно че угодно добавлять
        return Result.success("Тип коллекции: " + state.getCollection().getClass() + " Дата инициализации: " + state.getInitializationDate() +
                " Количество элементов: " + state.getCollection().size());
    }

    @Override
    public void parse(Console console) {

    }
}
