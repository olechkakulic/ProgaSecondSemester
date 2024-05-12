package olechka.lab7.commands;

import olechka.lab7.State;
import olechka.lab7.interaction.Console;
import olechka.lab7.models.StudyGroup;
import olechka.lab7.parsing.ObjectParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//вывести элементы коллекции в порядке убывания
@CommandDescription("вывести элементы коллекции в порядке убывания")
public class PrintDescendingCommand implements Command {
    @Override
    public Result execute(State state) {
        Collection<StudyGroup> collection = state.getCollection();
        List<StudyGroup> studyGroupList = new ArrayList<>(collection);
        Collections.sort(studyGroupList);
        String out = studyGroupList.stream()
                .map((s) -> ObjectParser.printObject(s))
                .collect(Collectors.joining());
//        String out = "";
//        for (StudyGroup s :
//                studyGroupList) {
//            out += ObjectParser.printObject(s);
//        }
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
