package olechka.lab7.commands;//package olechka.lab7.сommand;

import olechka.lab7.State;
import olechka.lab7.interaction.Console;
import olechka.lab7.models.StudyGroup;
import olechka.lab7.parsing.ObjectParser;

import java.util.Collection;
import java.util.List;

//удалить из коллекции все элементы меньшие чем заданный"
@CommandDescription("{element} удалить из коллекции все элементы, меньшие, чем заданный")
public class RemoveLowerCommand implements Command {
    private StudyGroup studyGroup;

    @Override
    public Result execute(State state) {
        Collection<StudyGroup> collection = state.getCollection();
        List<StudyGroup> lowerIdElement = collection.stream()
                .filter(state::isOwnedObject)
                .filter(s -> studyGroup.compareTo(s) > 0)
                .toList();
//        for (StudyGroup s :
//                collection) {
//            if (studyGroup.compareTo(s) > 0) {
//                lowerIdElement.add(s);
//            }
//        }
        if (lowerIdElement.isEmpty()) {
            return Result.error("Вам нечего удалять из коллекции блин:) ");
        } else {
            for (StudyGroup s : lowerIdElement) {
                state.remove(s);
            }
        }
        return Result.success("Было удалено элементов: " + lowerIdElement.size());
    }

    @Override
    public void parse(Console console) {
        console.nextLine();
        this.studyGroup = ObjectParser.createInteractive(StudyGroup.class, console, false);
    }
}
