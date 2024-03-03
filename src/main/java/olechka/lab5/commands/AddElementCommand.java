package olechka.lab5.commands;

import olechka.lab5.State;
import olechka.lab5.interaction.Console;
import olechka.lab5.models.Person;
import olechka.lab5.models.StudyGroup;
import olechka.lab5.parsing.ObjectParser;

public class AddElementCommand implements Command {
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
        state.addElement(studyGroup);
        state.notifyUpdate();
        return Result.success("Элемент с id: " + studyGroup.getId() + " успешно добавлен в коллекцию");
    }

    @Override
    public void parse(Console console) {
        console.nextLine();
        this.studyGroup = ObjectParser.createFromScanner(StudyGroup.class, console, false);
    }

}
