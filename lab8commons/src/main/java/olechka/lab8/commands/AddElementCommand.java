package olechka.lab8.commands;

import olechka.lab8.State;
import olechka.lab8.exceptions.CommandExecutionException;
import olechka.lab8.exceptions.CommandFailureType;
import olechka.lab8.models.Person;
import olechka.lab8.models.StudyGroup;

public class AddElementCommand extends Command {
    private StudyGroup studyGroup;

    public AddElementCommand() {
    }

    public AddElementCommand(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    @Override
    public void execute(State state) {
        Person groupAdmin = studyGroup.getGroupAdmin();
        if (groupAdmin != null) {
            boolean isSameKey = state.hasPersonWithPassportId(groupAdmin.getPassportID());
            if (isSameKey) {
                throw new CommandExecutionException(CommandFailureType.SAME_KEY);
            }
        }
        studyGroup.setUserId(userId);
        state.addElement(studyGroup);
    }

}
