package olechka.lab5.commands;//package olechka.lab5.сommand;

import olechka.lab5.State;
import olechka.lab5.interaction.Console;
import olechka.lab5.models.StudyCountComparator;
import olechka.lab5.models.StudyGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

//вывести значение поля studentsCount всех элементов в порядке убывания
public class PrintFieldDescendingCommand implements Command {
    @Override
    public Result execute(State state) {
        Collection<StudyGroup> collection = state.getCollection();
        List<StudyGroup> studyGroupList = new ArrayList<>(collection);
        Collections.sort(studyGroupList, new StudyCountComparator());
        List<Long> studentsCount = new ArrayList<>();
        for (StudyGroup s :
                collection) {
            studentsCount.add(s.getStudentsCount());
        }
        if (!collection.isEmpty()) {
            return Result.success("Значение поля studentsCount всех элементов в порядке убывания: " + studentsCount.toString());
        } else {
            return Result.success("Коллекция пуста, поэтому значение поля studentsCount всех элементов в порядке убывания: " + studentsCount.toString() + " - пустой массив. ");
        }

    }

    @Override
    public void parse(Console console) {

    }
}
