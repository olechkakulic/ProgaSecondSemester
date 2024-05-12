package olechka.lab7.commands;

import olechka.lab7.State;
import olechka.lab7.interaction.Console;
import olechka.lab7.models.StudyGroup;
import olechka.lab7.parsing.ObjectParser;

import java.util.Collection;
import java.util.stream.Collectors;

@CommandDescription("вывести в стандартный поток вывода все элементы коллекции в строковом представлении")
public class ShowCommand implements Command {

    @Override
    public Result execute(State state) {
        Collection<StudyGroup> collection = state.getCollection();
        if (collection.isEmpty()) {
            return Result.success("Коллекция пустая");
        }
        String out = collection.stream()
                .map((s) -> ObjectParser.printObject(s))
                .collect(Collectors.joining());
//        String out = "";
//        for (StudyGroup s :
//                collection) {
//            out += ObjectParser.printObject(s);
//        }
        return Result.success("Элементы коллекции: " + out);
    }

    @Override
    public void parse(Console console) {

    }
}
