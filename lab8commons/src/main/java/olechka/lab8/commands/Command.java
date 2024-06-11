package olechka.lab8.commands;

import lombok.Getter;
import lombok.Setter;
import olechka.lab8.State;
import olechka.lab8.models.StudyGroup;

import java.io.Serializable;
import java.util.Objects;

public abstract class Command implements Serializable {

    @Getter
    @Setter
    protected Long userId;
    //    статический вложенный класс, который не будет связан с объектами внешних классов после создания

    public abstract void execute(State state);

    protected boolean isOwnedObject(StudyGroup studyGroup) {
        return Objects.equals(studyGroup.getUserId(), userId);
    }
}
