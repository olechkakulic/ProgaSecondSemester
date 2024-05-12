package olechka.lab7.commands;

import olechka.lab7.State;
import olechka.lab7.interaction.Console;
import olechka.lab7.models.StudyGroup;
import olechka.lab7.parsing.ObjectParser;

import java.util.Collection;
import java.util.Optional;

@CommandDescription("{id (тип int)} {element} обновить значение элемента коллекции, id которого равен заданному")
public class UpdateElementCommand implements Command {
    public long id;
    private StudyGroup studyGroup;

    @Override
    public Result execute(State state) {
        Collection<StudyGroup> collection = state.getCollection();
        Optional<StudyGroup> prevValue = collection.stream().filter(s -> s.getId() == id).findAny();
//        for (StudyGroup s :
//                collection) {
//            if (id == s.getId()) {
//                prevValue = s;
//
//            }
//        }
        if (prevValue.isEmpty()) {
            return Result.success("Вы пытаетесь обновить то, чего нет. Так делать нельзя!");
        }
        if (state.isOwnedObject(prevValue.get())) {
            state.replace(prevValue.get(), studyGroup);
            return Result.success("Вы обновили элемент с индексом: " + this.id);
        } else {
            return Result.error("У вас нет прав обновлять данный объект");
        }

    }

    @Override
    public void parse(Console console) {
        id = console.getRemainingIntArgument();
        this.studyGroup = ObjectParser.createInteractive(StudyGroup.class, console, false);
    }
}
