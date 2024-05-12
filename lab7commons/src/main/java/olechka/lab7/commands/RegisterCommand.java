package olechka.lab7.commands;

import olechka.lab7.State;
import olechka.lab7.interaction.Console;

public class RegisterCommand implements Command {
    @Override
    public Result execute(State state) {
        return Result.success("Вы успешно зарегистрировались!");
    }

    @Override
    public void parse(Console console) {

    }
}
