package olechka.lab8.commands;//package olechka.lab7.сommand;

import olechka.lab8.State;
import olechka.lab8.exceptions.CommandExecutionException;
import olechka.lab8.exceptions.CommandFailureType;
import olechka.lab8.models.Person;
import olechka.lab8.models.StudyGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

//добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции.
public class AddIfMinCommand extends Command {
    private StudyGroup studyGroup;


    @Override
    public void execute(State state) {
        Person groupAdmin = studyGroup.getGroupAdmin();
        if (groupAdmin != null) {
            boolean isSameKey = state.hasPersonWithPassportId(groupAdmin.getPassportID());
            if (isSameKey) {
                throw new CommandExecutionException(CommandFailureType.SAME_KEY);
            }
        }
        Collection<StudyGroup> collection = state.getCollection();
        List<StudyGroup> collectionStudyGroup = new ArrayList<>(collection);
        Collections.sort(collectionStudyGroup);
        if (collectionStudyGroup.isEmpty()) {
            throw new CommandExecutionException(CommandFailureType.NO_COMPARISON_CANDIDATE);
        }
        StudyGroup firstElement = collectionStudyGroup.get(0);
        if (studyGroup.compareTo(firstElement) < 0) {
            studyGroup.setUserId(userId);
            state.addElement(studyGroup);
        } else {
            throw new CommandExecutionException(CommandFailureType.COMPARISON_CONSTRAINT_FAILED);
        }
    }

}
