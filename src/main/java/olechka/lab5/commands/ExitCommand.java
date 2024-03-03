package olechka.lab5.commands;

import olechka.lab5.State;
import olechka.lab5.interaction.Console;

;

public class ExitCommand implements Command {
    @Override
    public Result execute(State state) {
        state.setExitRequested();
        return Result.success("Совершен выход из программы");
    }

    @Override
    public void parse(Console console) {
    }
}
