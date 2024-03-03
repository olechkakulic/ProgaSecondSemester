package olechka.lab5.commands;

import olechka.lab5.State;
import olechka.lab5.interaction.Console;
import olechka.lab5.models.StudyGroup;
import olechka.lab5.parsing.ObjectParser;

import java.util.Collection;
import java.util.Objects;

// вывести элементы значения поля studentsCount которых равно заданному
public class FilterByStudentsCount implements Command {
    public Integer studentCount;

    @Override
    public Result execute(State state) {
        Collection<StudyGroup> collection = state.getCollection();
        String out = "";
        for (StudyGroup s :
                collection) {
            if (Objects.equals(s.getStudentsCount(), studentCount)) {
                out += ObjectParser.printObject(s);
            }
        }
        if (out.isEmpty()) {
            return Result.error("Нет элемента с таким studentCount");
        }
        return Result.success("Элементы, значения поля studentsCount которых равно заданному: " + out);
    }

    @Override
    public void parse(Console console) {
        studentCount = console.nextInt();
    }
}

