package olechka.lab5.models;

import olechka.lab5.parsing.annotations.BiggerThan;
import olechka.lab5.parsing.annotations.HumanDescription;
import olechka.lab5.parsing.annotations.IgnoreInput;
import olechka.lab5.parsing.annotations.NonNull;

import java.time.LocalDate;

public class StudyGroup implements Comparable<StudyGroup> {
    @IgnoreInput
    @NonNull
    @BiggerThan(0)
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @NonNull
    @HumanDescription(value = "имя", format = "непустая строка")
    private String name; //Поле не может быть null, Строка не может быть пустой
    @NonNull
    @HumanDescription("координаты")
    private Coordinates coordinates; //Поле не может быть null
    @NonNull
    @HumanDescription("дата создания группы")
    @IgnoreInput
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    @NonNull
    @BiggerThan(0)
    @HumanDescription(value = "количество студентов", format = "целое число > 0")
    private Integer studentsCount; //Значение поля должно быть больше 0, Поле не может быть null
    @BiggerThan(0)
    @HumanDescription(value = "количество переведенных студентов", format = "целое число > 0")
    private int transferredStudents; //Значение поля должно быть больше 0
    @NonNull
    @HumanDescription("форма обучения")
    private FormOfEducation formOfEducation; //Поле не может быть null
    @HumanDescription(value = "семестр", format = "значение может отсутствовать")
    private Semester semesterEnum; //Поле может быть null
    @HumanDescription(value = "админ группы", format = "значение может отсутствовать")
    private Person groupAdmin; //Поле может быть null

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Integer getStudentsCount() {
        return studentsCount;
    }

    public int getTransferredStudents() {
        return transferredStudents;
    }

    public FormOfEducation getFormOfEducation() {
        return formOfEducation;
    }

    public Semester getSemesterEnum() {
        return semesterEnum;
    }

    public Person getGroupAdmin() {
        return groupAdmin;
    }

    @Override
    public String toString() {
        return "StudyGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", studentsCount=" + studentsCount +
                ", transferredStudents=" + transferredStudents +
                ", formOfEducation=" + formOfEducation +
                ", semesterEnum=" + semesterEnum +
                ", groupAdmin=" + groupAdmin +
                '}';
    }

    @Override
    public int compareTo(StudyGroup o) {
        return name.compareTo(o.getName());
    }
}


