package olechka.lab8.server;

import lombok.Getter;
import olechka.lab8.MemoryState;
import olechka.lab8.State;
import olechka.lab8.models.StudyGroup;
import org.hibernate.SessionFactory;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class DatabaseState implements State {
    private final SessionFactory sessionFactory;
    @Getter
    private final MemoryState memoryState;

    public DatabaseState(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        memoryState = new MemoryState();
        sessionFactory.inTransaction(session -> {
            List<StudyGroup> list = session.createQuery("from StudyGroup", StudyGroup.class).list();
            memoryState.getMutableCollection().addAll(list);
        });
    }

    @Override
    public boolean hasPersonWithPassportId(String passportId) {
        return memoryState.hasPersonWithPassportId(passportId);
    }

    @Override
    public Collection<StudyGroup> getCollection() {
        return memoryState.getCollection();
    }

    @Override
    public void addElement(StudyGroup studyGroup) {
        if (studyGroup.getUserId() == null) {
            throw new IllegalArgumentException("studyGroup should have userId set");
        }
        studyGroup.setCreationDate(LocalDateTime.now());
        // добавление элемента в бд
        sessionFactory.inTransaction(session -> {
            session.persist(studyGroup);
            session.flush();
        });
        // Обновлять состояние коллекции в памяти только при успешном добавлении объекта в БД
        memoryState.addElement(studyGroup);
    }

    @Override
    public void remove(StudyGroup studyGroup) {
        sessionFactory.inTransaction(session -> {
            session.remove(studyGroup);
            session.flush();
        });
        memoryState.remove(studyGroup);
    }

    @Override
    public void replace(StudyGroup lastStudyGroup, StudyGroup studyGroup) {
        memoryState.replace(lastStudyGroup, studyGroup);
        sessionFactory.inTransaction(session -> {
            StudyGroup persistStudyGroup = session.merge(studyGroup);
            if (persistStudyGroup != studyGroup) {
                memoryState.replace(studyGroup, persistStudyGroup);
            }
            session.flush();
        });
    }

    @Override
    public boolean clear(Long userId) {
        sessionFactory.inTransaction(session -> {
            session.clear();
            session.createQuery("delete from StudyGroup where userId = :userId", null)
                    .setParameter("userId", userId)
                    .executeUpdate();
            session.flush();
        });
        return memoryState.clear(userId);
    }
}
