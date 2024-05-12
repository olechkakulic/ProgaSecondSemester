package olechka.lab7.commands;

import olechka.lab7.State;
import olechka.lab7.interaction.Console;
import olechka.lab7.models.Person;
import olechka.lab7.models.StudyGroup;
import olechka.lab7.parsing.ObjectParser;

@CommandDescription("добавить новый элемент в коллекцию")
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
        return Result.success("Элемент с id: " + studyGroup.getId() + " успешно добавлен в коллекцию");
    }

    @Override
    public void parse(Console console) {
        console.nextLine();
        this.studyGroup = ObjectParser.createInteractive(StudyGroup.class, console, false);
    }

}
