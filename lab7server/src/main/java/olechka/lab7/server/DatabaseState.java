package olechka.lab7.server;

import olechka.lab7.State;
import olechka.lab7.models.StudyGroup;
import org.hibernate.SessionFactory;

import java.time.LocalDateTime;
import java.util.List;

public class DatabaseState extends State {
    private final SessionFactory sessionFactory;

    public DatabaseState(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        sessionFactory.inTransaction(session -> {
            List<StudyGroup> list = session.createQuery("from StudyGroup", StudyGroup.class).list();
            collection.addAll(list);
        });
    }

    @Override
    public void addElement(StudyGroup studyGroup) {
        studyGroup.setUserId(currentUserId);
        studyGroup.setCreationDate(LocalDateTime.now());
//        добавление элемента в бд
        sessionFactory.inTransaction(session -> {
            session.persist(studyGroup);
            session.flush();
        });
        super.addElement(studyGroup);
    }

    @Override
    public void remove(StudyGroup studyGroup) {
        sessionFactory.inTransaction(session -> {
            session.remove(studyGroup);
            session.flush();
        });
        super.remove(studyGroup);
    }

    @Override
    public void replace(StudyGroup lastStudyGroup, StudyGroup studyGroup) {
        super.replace(lastStudyGroup, studyGroup);
        sessionFactory.inTransaction(session -> {
            StudyGroup persistStudyGroup = session.merge(studyGroup);
            if (persistStudyGroup != studyGroup) {
                super.replace(studyGroup, persistStudyGroup);
            }
            session.flush();
        });
    }

    @Override
    public boolean clear() {
        sessionFactory.inTransaction(session -> {
            session.clear();
            session.createQuery("delete from StudyGroup where userId = :userId", null)
                    .setParameter("userId", currentUserId)
                    .executeUpdate();
            session.flush();
        });
        return super.clear();
    }
}
