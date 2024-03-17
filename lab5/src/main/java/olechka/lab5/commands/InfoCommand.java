package olechka.lab5.commands;

import olechka.lab5.State;
import olechka.lab5.interaction.Console;

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
