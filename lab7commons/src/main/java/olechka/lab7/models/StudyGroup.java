package olechka.lab7.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import olechka.lab7.parsing.annotations.BiggerThan;
import olechka.lab7.parsing.annotations.HumanDescription;
import olechka.lab7.parsing.annotations.IgnoreInput;
import olechka.lab7.parsing.annotations.NonNull;

import java.io.Serializable;
import java.time.LocalDateTime;

@ToString
@Entity
@Table(name = "groups")
public class StudyGroup implements Comparable<StudyGroup>, Serializable {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @IgnoreInput
    @NonNull
    @BiggerThan(0)
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @Getter
    @Column(nullable = false)
    @NonNull
    @HumanDescription(value = "имя", format = "непустая строка")
    private String name; //Поле не может быть null, Строка не может быть пустой

    @Getter
    @Column(nullable = false)
    @NonNull
    @HumanDescription("координаты")
    private Coordinates coordinates; //Поле не может быть null

    @Getter
    @Setter
    @Column(nullable = false)
    @NonNull
    @HumanDescription(value = "дата создания группы")
    @IgnoreInput
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @Getter
    @Column(nullable = false)
    @NonNull
    @BiggerThan(0)
    @HumanDescription(value = "количество студентов", format = "целое число > 0")
    private Long studentsCount; //Значение поля должно быть больше 0, Поле не может быть null

    @Getter
    @Column(nullable = false)
    @BiggerThan(0)
    @HumanDescription(value = "количество переведенных студентов", format = "целое число > 0")
    private long transferredStudents; //Значение поля должно быть больше 0

    @Getter
    @Column(nullable = false)
    @NonNull
    @HumanDescription("форма обучения")
    private FormOfEducation formOfEducation; //Поле не может быть null

    @Getter
    @Column
    @HumanDescription(value = "семестр", format = "значение может отсутствовать")
    private Semester semesterEnum; //Поле может быть null

    @Getter
    @Column
    @HumanDescription(value = "админ группы", format = "значение может отсутствовать")
    private Person groupAdmin; //Поле может быть null

    @Getter
    @Setter
    @Column
    @IgnoreInput
    private Long userId;

    @Override
    public int compareTo(StudyGroup o) {
        return name.compareTo(o.getName());
    }
}


