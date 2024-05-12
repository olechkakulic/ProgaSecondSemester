package olechka.lab7.commands;//package olechka.lab7.сommand;

import olechka.lab7.State;
import olechka.lab7.interaction.Console;
import olechka.lab7.models.StudyGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//вывести значение поля studentsCount всех элементов в порядке убывания
@CommandDescription("вывести значения поля studentsCount все элементов в порядке убывания")
public class PrintFieldDescendingCommand implements Command {
    @Override
    public Result execute(State state) {
        Collection<StudyGroup> collection = state.getCollection();
        List<StudyGroup> studyGroupList = new ArrayList<>(collection);
        studyGroupList.sort((o1, o2) -> {
                    return Long.compare(o1.getStudentsCount(), o2.getStudentsCount());
                }
        );
        List<Long> studentsCount = studyGroupList.stream().map(s -> s.getStudentsCount()).toList();
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
