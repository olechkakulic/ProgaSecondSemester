package olechka.lab5.commands;

import olechka.lab5.State;
import olechka.lab5.interaction.Console;
import olechka.lab5.models.StudyGroup;

import java.util.Collection;

public class RemoveElementCommand implements Command {
    private int id;

    @Override
    public Result execute(State state) {
        StudyGroup prevValue = null;
        Collection<StudyGroup> collection = state.getCollection();
        for (StudyGroup s :
                collection) {
            if (s.getId() == id) {
                prevValue = s;
            }
        }
        if (prevValue == null) {
            return Result.error("Вы пытаетесь удалить то, чего нет. Так делать нельзя!");
        }
        collection.remove(prevValue);
        state.notifyUpdate();
        return Result.success("Вы удалили элемент с индексом: " + this.id);
    }

    @Override
    public void parse(Console console) {
        id = console.getRemainingIntArgument();
    }
}
