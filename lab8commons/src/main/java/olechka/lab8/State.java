package olechka.lab8;

import olechka.lab8.models.StudyGroup;

import java.util.Collection;

public interface State {
    Collection<StudyGroup> getCollection();

    void replace(StudyGroup lastStudyGroup, StudyGroup studyGroup);

    void remove(StudyGroup studyGroup);

    boolean clear(Long userId);

    void addElement(StudyGroup studyGroup);

    boolean hasPersonWithPassportId(String passportId);

}
