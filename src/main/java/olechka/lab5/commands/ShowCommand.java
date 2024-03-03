package olechka.lab5.commands;

import olechka.lab5.State;
import olechka.lab5.interaction.Console;
import olechka.lab5.models.StudyGroup;
import olechka.lab5.parsing.ObjectParser;

import java.util.Collection;

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
