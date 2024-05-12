package olechka.lab7.commands;

import olechka.lab7.State;
import olechka.lab7.interaction.Console;
import olechka.lab7.models.StudyGroup;

import java.util.Collection;
import java.util.Optional;

@CommandDescription("{id (тип int)} удалить элемент из коллекции по его id")
public class RemoveElementCommand implements Command {
    private int id;

    @Override
    public Result execute(State state) {
        Collection<StudyGroup> collection = state.getCollection();
        Optional<StudyGroup> prevValue = collection.stream().
                filter(s -> s.getId() == id).findAny();
        if (prevValue.isEmpty()) {
            return Result.error("Вы пытаетесь удалить то, чего нет. Так делать нельзя!");
        }
        if (state.isOwnedObject(prevValue.get())) {
            state.remove(prevValue.get());
            return Result.success("Вы удалили элемент с индексом: " + this.id);
        } else {
            return Result.error("У вас нет прав удалять данный объект");
        }
    }

    @Override
    public void parse(Console console) {
        id = console.getRemainingIntArgument();
    }
}
