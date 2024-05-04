package olechka.lab6.commands;

import olechka.lab6.State;
import olechka.lab6.interaction.Console;
import olechka.lab6.models.StudyGroup;

import java.util.Collection;
import java.util.Optional;

@CommandDescription("{id (тип int)} удалить элемент из коллекции по его id")
public class RemoveElementCommand implements Command {
    private int id;

    @Override
    public Result execute(State state) {
        Collection<StudyGroup> collection = state.getCollection();
        Optional<StudyGroup> prevValue = collection.stream().filter(s -> s.getId() == id).findAny();
        if (prevValue.isEmpty()) {
            return Result.error("Вы пытаетесь удалить то, чего нет. Так делать нельзя!");
        }
        collection.remove(prevValue.get());
        state.notifyUpdate();
        return Result.success("Вы удалили элемент с индексом: " + this.id);
    }

    @Override
    public void parse(Console console) {
        id = console.getRemainingIntArgument();
    }
}
