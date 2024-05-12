package olechka.lab7.commands;

import olechka.lab7.State;
import olechka.lab7.interaction.Console;

public class LoginCommand implements Command {
    @Override
    public Result execute(State state) {
        return Result.success("Вы успешно вошли.");
    }

    @Override
    public void parse(Console console) {

    }
}
