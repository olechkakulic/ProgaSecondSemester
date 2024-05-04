package olechka.lab6.commands;

import olechka.lab6.State;
import olechka.lab6.interaction.Console;
import olechka.lab6.models.StudyGroup;
import olechka.lab6.parsing.ObjectParser;

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
        collection.remove(prevValue.get());
        studyGroup.setId(id);
        studyGroup.setCreationDate(prevValue.get().getCreationDate());
        collection.add(studyGroup);
        state.notifyUpdate();
        return Result.success("Элемент успешно удален. ");
    }

    @Override
    public void parse(Console console) {
        id = console.getRemainingIntArgument();
        this.studyGroup = ObjectParser.createInteractive(StudyGroup.class, console, false);
    }
}
