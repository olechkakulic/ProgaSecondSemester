package olechka.lab7.commands;

import olechka.lab7.State;
import olechka.lab7.interaction.Console;
import olechka.lab7.models.StudyGroup;
import olechka.lab7.parsing.ObjectParser;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

// вывести элементы значения поля studentsCount которых равно заданному
@CommandDescription("{students_count (тип int)} вывести элементы, значение поля studentsCount которых равно заданному")
public class FilterByStudentsCount implements Command {
    public Long studentCount;

    @Override
    public Result execute(State state) {
        Collection<StudyGroup> collection = state.getCollection();
        String out = collection.stream().filter(element -> Objects.equals(element.getStudentsCount(), studentCount))
                .map((s) -> ObjectParser.printObject(s))
                .collect(Collectors.joining());
        if (collection.isEmpty()) {
            return Result.success("Коллекция пуста. Вам не с чем сравнивать поле studentsCount");
        } else if (out.isEmpty()) {
            return Result.error("Нет элемента с таким studentCount");
        } else {
            return Result.success("Элементы, значения поля studentsCount которых равно заданному: " + out);
        }
    }

    @Override
    public void parse(Console console) {
        studentCount = console.getRemainingLongArgument();
    }
}

