package olechka.lab6;

import olechka.lab6.models.StudyGroup;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

//содержит внутри себя объекты имеющие какие-то состояния
public class State {
    private final Collection<StudyGroup> collection;
    private final Date initializationDate;
    private final CommandManager commandManager;
    private boolean isExitRequested;

    private String saveFileName;
    private long lastId;
    private Date updateDate;
    private LocalDate creationDate;

    public State(String saveFileName) {
        this.saveFileName = saveFileName;
        collection = new LinkedHashSet<>();
//        создаст текущую дату
        initializationDate = new Date();
        commandManager = new CommandManager();
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public void notifyUpdate() {
        updateDate = new Date();
    }

    public Collection<StudyGroup> getCollection() {
        return collection;
    }

    public Date getInitializationDate() {
        return initializationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    private long generateId() {
        if (collection.isEmpty()) {
            lastId = 0;
            return ++lastId;
        } else {
            return ++lastId;
        }
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public boolean isExitRequested() {
        return isExitRequested;
    }

    public void setExitRequested() {
        isExitRequested = true;
    }

    private LocalDateTime generateCreationDate() {
        long minTime = LocalDateTime.of(1970, 1, 1, 0, 0).toEpochSecond(ZoneOffset.UTC);
        long maxTime = LocalDateTime.of(2053, 1, 1, 0, 0).toEpochSecond(ZoneOffset.UTC);
        long randomDay = ThreadLocalRandom.current().nextLong(minTime, maxTime);
        LocalDateTime randomDate = LocalDateTime.ofEpochSecond(randomDay, 0, ZoneOffset.UTC);
        return randomDate;
    }

    //функция добавления элемента, которая есть тут потому что появилась проблема с ебаным в жопу айдишником.
    public void addElement(StudyGroup studyGroup) {
        if (studyGroup.getId() != null) {
            if (studyGroup.getId() > lastId) {
                lastId = studyGroup.getId();
            }
        } else {

            studyGroup.setId(generateId());
        }
        if (studyGroup.getCreationDate() == null) {
            studyGroup.setCreationDate(generateCreationDate());
        }
        collection.add(studyGroup);
    }

    public boolean hasPersonWithPassportId(String passportId) {
        return collection.stream().map(s -> s.getGroupAdmin()).anyMatch(g -> g != null && Objects.equals(g.getPassportID(), passportId));
//        for (StudyGroup element : collection) {
//            Person elementGroupAdmin = element.getGroupAdmin();
//            if (elementGroupAdmin != null) {
//                if (elementGroupAdmin.getPassportID() != null && elementGroupAdmin.getPassportID().equals(passportId)) {
//                    return true;
//                }
//            }
//        }
//        return false;
    }


}
