package olechka.lab5.models;

import java.util.Comparator;

public class StudyCountComparator implements Comparator<StudyGroup> {

    @Override
    public int compare(StudyGroup o1, StudyGroup o2) {
        return Long.compare(o1.getStudentsCount(), o2.getStudentsCount());
    }
}