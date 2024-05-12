package olechka.lab7;

import lombok.Getter;
import lombok.Setter;
import olechka.lab7.models.StudyGroup;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

//содержит внутри себя объекты имеющие какие-то состояния
public class State {
    protected final Collection<StudyGroup> collection;
    @Getter
    protected final Date initializationDate;
    @Getter
    protected final CommandManager commandManager;
    protected boolean isExitRequested;
    @Getter
    @Setter
    protected Long currentUserId;

    private long lastId;

    public State() {
        collection = new LinkedHashSet<>();
//        создаст текущую дату
        initializationDate = new Date();
        commandManager = new CommandManager();
    }

    public Collection<StudyGroup> getCollection() {
        return Collections.unmodifiableCollection(collection);
    }

    public void replace(StudyGroup lastStudyGroup, StudyGroup studyGroup) {
        collection.remove(lastStudyGroup);
        studyGroup.setId(lastStudyGroup.getId());
        studyGroup.setCreationDate(lastStudyGroup.getCreationDate());
        studyGroup.setUserId(lastStudyGroup.getUserId());
        collection.add(studyGroup);
    }

    public void remove(StudyGroup studyGroup) {
        collection.remove(studyGroup);
    }

    public boolean clear() {
        return collection.removeIf(studyGroup -> Objects.equals(studyGroup.getUserId(), currentUserId));
    }


    private long generateId() {
        if (collection.isEmpty()) {
            lastId = 0;
            return ++lastId;
        } else {
            return ++lastId;
        }
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

    public boolean isOwnedObject(StudyGroup studyGroup) {
        return Objects.equals(studyGroup.getUserId(), currentUserId);
    }


}
