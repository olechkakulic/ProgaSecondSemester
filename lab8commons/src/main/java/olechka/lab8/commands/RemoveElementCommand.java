package olechka.lab8.commands;

import olechka.lab8.State;
import olechka.lab8.exceptions.CommandExecutionException;
import olechka.lab8.exceptions.CommandFailureType;
import olechka.lab8.models.StudyGroup;

import java.util.Collection;
import java.util.Optional;

public class RemoveElementCommand extends Command {
    private long id;

    public RemoveElementCommand() {
    }

    public RemoveElementCommand(long id) {
        this.id = id;
    }

    @Override
    public void execute(State state) {
        Collection<StudyGroup> collection = state.getCollection();
        Optional<StudyGroup> prevValue = collection.stream().
                filter(s -> s.getId() == id).findAny();
        if (prevValue.isEmpty()) {
            throw new CommandExecutionException(CommandFailureType.NO_ELEMENT_FOUND);
        }
        if (isOwnedObject(prevValue.get())) {
            state.remove(prevValue.get());
        } else {
            throw new CommandExecutionException(CommandFailureType.NO_RIGHTS);
        }
    }

}
