package olechka.lab6.commands;

import olechka.lab6.State;
import olechka.lab6.interaction.Console;
import olechka.lab6.models.StudyGroup;
import olechka.lab6.parsing.ObjectParser;

import java.util.Collection;

@CommandDescription("вывести в стандартный поток вывода все элементы коллекции в строковом представлении")
public class ShowCommand implements Command {

    @Override
    public Result execute(State state) {
        Collection<StudyGroup> collection = state.getCollection();
        if (collection.isEmpty()) {
            return Result.success("Коллекция пустая");
        }
        String out = "";
        for (StudyGroup s :
                collection) {
            out += ObjectParser.printObject(s);
        }
        return Result.success("Элементы коллекции: " + out);
    }

    @Override
    public void parse(Console console) {

    }
}
