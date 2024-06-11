package olechka.lab8.commands;//package olechka.lab7.сommand;

import olechka.lab8.State;
import olechka.lab8.exceptions.CommandExecutionException;
import olechka.lab8.exceptions.CommandFailureType;
import olechka.lab8.models.StudyGroup;

import java.util.Collection;
import java.util.List;

//удалить из коллекции все элементы меньшие чем заданный"
public class RemoveLowerCommand extends Command {
    private StudyGroup studyGroup;

    @Override
    public void execute(State state) {
        Collection<StudyGroup> collection = state.getCollection();
        List<StudyGroup> lowerIdElement = collection.stream()
                .filter((s) -> isOwnedObject(s))
                .filter((s) -> studyGroup.compareTo(s) > 0)
                .toList();
        if (lowerIdElement.isEmpty()) {
            throw new CommandExecutionException(CommandFailureType.COMPARISON_CONSTRAINT_FAILED);
        } else {
            for (StudyGroup s : lowerIdElement) {
                state.remove(s);
            }
        }
    }

}
