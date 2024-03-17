package olechka.lab5.commands;//package olechka.lab5.сommand;

import olechka.lab5.State;
import olechka.lab5.interaction.Console;
import olechka.lab5.models.Person;
import olechka.lab5.models.StudyGroup;
import olechka.lab5.parsing.ObjectParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

//добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции.
public class AddIfMinCommand implements Command {
    private StudyGroup studyGroup;


    @Override
    public Result execute(State state) {
        Person groupAdmin = studyGroup.getGroupAdmin();
        if (groupAdmin != null) {
            boolean result = state.hasPersonWithPassportId(groupAdmin.getPassportID());
            if (result) {
                return Result.error("Вы пытались добавить groupAdmin с уже существующем passportId");
            }
        }
        Collection<StudyGroup> collection = state.getCollection();
        List<StudyGroup> collectionStudyGroup = new ArrayList<>(collection);
        Collections.sort(collectionStudyGroup);
        if (collectionStudyGroup.isEmpty()) {
            return Result.error("Вам не с чем сравнивать");
        }
        StudyGroup firstElement = collectionStudyGroup.get(0);
        if (studyGroup.compareTo(firstElement) < 0) {
            state.addElement(studyGroup);
        } else {
            return Result.error("Не получилось добавить элемент в коллекцию. Значение вашего элемента больше, чем у наименьшего элемента коллекции. ");
        }
        return Result.success("Вы успешно добавили элемент коллекции");
    }

    @Override
    public void parse(Console console) {
        console.nextLine();
        this.studyGroup = ObjectParser.createInteractive(StudyGroup.class, console, false);
    }
}
