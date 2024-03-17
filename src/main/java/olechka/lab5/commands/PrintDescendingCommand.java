package olechka.lab5.commands;

import olechka.lab5.State;
import olechka.lab5.interaction.Console;
import olechka.lab5.models.StudyGroup;
import olechka.lab5.parsing.ObjectParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

//вывести элементы коллекции в порядке убывания
public class PrintDescendingCommand implements Command {
    @Override
    public Result execute(State state) {
        Collection<StudyGroup> collection = state.getCollection();
        List<StudyGroup> studyGroupList = new ArrayList<>(collection);
        Collections.sort(studyGroupList);
        String out = "";
        for (StudyGroup s :
                studyGroupList) {
            out += ObjectParser.printObject(s);
        }
        if (!collection.isEmpty()) {
            return Result.success("Элементы коллекции в порядке убывания: " + out);
        } else {
            return Result.success("Коллекция пуста. К сожалению, выводить нечего. ");
        }
    }

    @Override
    public void parse(Console console) {

    }
}
