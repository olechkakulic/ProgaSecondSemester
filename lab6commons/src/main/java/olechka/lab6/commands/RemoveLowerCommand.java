package olechka.lab6.commands;//package olechka.lab6.сommand;

import olechka.lab6.State;
import olechka.lab6.interaction.Console;
import olechka.lab6.models.StudyGroup;
import olechka.lab6.parsing.ObjectParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//удалить из коллекции все элементы меньшие чем заданный"
@CommandDescription("{element} удалить из коллекции все элементы, меньшие, чем заданный")
public class RemoveLowerCommand implements Command {
    private StudyGroup studyGroup;

    @Override
    public Result execute(State state) {
        List<StudyGroup> lowerIdElement = new ArrayList<>();
        Collection<StudyGroup> collection = state.getCollection();
        for (StudyGroup s :
                collection) {
            if (studyGroup.compareTo(s) > 0) {
                lowerIdElement.add(s);
            }
        }
        if (lowerIdElement.isEmpty()) {
            return Result.error("Вам нечего удалять из коллекции блин:) ");
        } else {
            collection.removeAll(lowerIdElement);
        }
        return Result.success("Было удалено элементов: " + lowerIdElement.size());
    }

    @Override
    public void parse(Console console) {
        console.nextLine();
        this.studyGroup = ObjectParser.createInteractive(StudyGroup.class, console, false);
    }
}
