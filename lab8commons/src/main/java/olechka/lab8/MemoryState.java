package olechka.lab8;

import olechka.lab8.models.StudyGroup;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

//содержит внутри себя объекты имеющие какие-то состояния
public class MemoryState implements State, Serializable {
    private final Collection<StudyGroup> collection;

    private long lastId;

    public MemoryState() {
        collection = new LinkedHashSet<>();
    }

    public Collection<StudyGroup> getMutableCollection() {
        return collection;
    }

    @Override
    public Collection<StudyGroup> getCollection() {
        return Collections.unmodifiableCollection(collection);
    }

    @Override
    public void replace(StudyGroup lastStudyGroup, StudyGroup studyGroup) {
        collection.remove(lastStudyGroup);
        studyGroup.setId(lastStudyGroup.getId());
        studyGroup.setCreationDate(lastStudyGroup.getCreationDate());
        studyGroup.setUserId(lastStudyGroup.getUserId());
        collection.add(studyGroup);
    }

    @Override
    public void remove(StudyGroup studyGroup) {
        collection.remove(studyGroup);
    }

    @Override
    public boolean clear(Long userId) {
        return collection.removeIf(studyGroup -> Objects.equals(studyGroup.getUserId(), userId));
    }


    private long generateId() {
        if (collection.isEmpty()) {
            lastId = 0;
            return ++lastId;
        } else {
            return ++lastId;
        }
    }

    private LocalDateTime generateCreationDate() {
        long minTime = LocalDateTime.of(1970, 1, 1, 0, 0).toEpochSecond(ZoneOffset.UTC);
        long maxTime = LocalDateTime.of(2053, 1, 1, 0, 0).toEpochSecond(ZoneOffset.UTC);
        long randomDay = ThreadLocalRandom.current().nextLong(minTime, maxTime);
        LocalDateTime randomDate = LocalDateTime.ofEpochSecond(randomDay, 0, ZoneOffset.UTC);
        return randomDate;
    }

    //функция добавления элемента, которая есть тут потому что появилась проблема с ебаным в жопу айдишником.
    @Override
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

    @Override
    public boolean hasPersonWithPassportId(String passportId) {
        return collection.stream().map((s) -> s.getGroupAdmin())
                .anyMatch(g -> g != null && Objects.equals(g.getPassportID(), passportId));
    }
}
