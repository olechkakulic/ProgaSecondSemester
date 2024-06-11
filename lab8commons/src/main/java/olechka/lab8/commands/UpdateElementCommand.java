package olechka.lab8.commands;

import olechka.lab8.State;
import olechka.lab8.exceptions.CommandExecutionException;
import olechka.lab8.exceptions.CommandFailureType;
import olechka.lab8.models.StudyGroup;

import java.util.Collection;
import java.util.Optional;

public class UpdateElementCommand extends Command {
    private long id;
    private StudyGroup studyGroup;

    public UpdateElementCommand() {
    }

    public UpdateElementCommand(long id, StudyGroup studyGroup) {
        this.id = id;
        this.studyGroup = studyGroup;
    }

    @Override
    public void execute(State state) {
        Collection<StudyGroup> collection = state.getCollection();
        Optional<StudyGroup> prevValue = collection.stream().filter(s -> s.getId() == id).findAny();
        if (prevValue.isEmpty()) {
            throw new CommandExecutionException(CommandFailureType.NO_ELEMENT_FOUND);
        }
        if (isOwnedObject(prevValue.get())) {
            state.replace(prevValue.get(), studyGroup);
        } else {
            throw new CommandExecutionException(CommandFailureType.NO_RIGHTS);
        }

    }

}
