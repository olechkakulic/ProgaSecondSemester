package olechka.lab8.commands;

import olechka.lab8.State;
import olechka.lab8.exceptions.CommandExecutionException;
import olechka.lab8.exceptions.CommandFailureType;

public class ClearCommand extends Command {

    @Override
    public void execute(State state) {
        if (!state.clear(userId)) {
            throw new CommandExecutionException(CommandFailureType.NO_OWN_ELEMENTS);
        }
    }

}
