package olechka.lab7.commands;

import olechka.lab7.State;
import olechka.lab7.interaction.Console;

;

@CommandDescription("завершить программу (без сохранения в файл)")
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
