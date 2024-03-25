package olechka.lab6.commands;

import olechka.lab6.State;
import olechka.lab6.interaction.Console;

@CommandDescription("вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)")
public class InfoCommand implements Command {

    @Override
    public Result execute(State state) {
//        к строке можно че угодно добавлять
        return Result.success("Тип коллекции: " + state.getCollection().getClass() + " Дата инициализации: " + state.getInitializationDate() +
                " Количество элементов: " + state.getCollection().size() + " Дата обновления: " + state.getUpdateDate());
    }

    @Override
    public void parse(Console console) {

    }
}
