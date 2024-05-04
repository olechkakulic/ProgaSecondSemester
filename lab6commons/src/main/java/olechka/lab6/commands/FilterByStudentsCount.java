package olechka.lab6.commands;

import olechka.lab6.State;
import olechka.lab6.interaction.Console;
import olechka.lab6.models.StudyGroup;
import olechka.lab6.parsing.ObjectParser;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// вывести элементы значения поля studentsCount которых равно заданному
@CommandDescription("{students_count (тип int)} вывести элементы, значение поля studentsCount которых равно заданному")
public class FilterByStudentsCount implements Command {
    public Long studentCount;

    @Override
    public Result execute(State state) {
        List<StudyGroup> collection = (List<StudyGroup>) state.getCollection();
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

