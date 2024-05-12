package olechka.lab7.models;

import olechka.lab7.parsing.annotations.BiggerThan;
import olechka.lab7.parsing.annotations.HumanDescription;
import olechka.lab7.parsing.annotations.NonNull;

import java.io.Serializable;
import java.time.LocalDate;

public class Person implements Serializable {
    @NonNull
    @HumanDescription(value = "имя", format = "непустая строка")
    private String name; //Поле не может быть null, Строка не может быть пустой
    @HumanDescription(value = "дату рождения", format = "строка вида 00.00.00, значение может отсутствовать")
    private LocalDate birthday; //Поле может быть null
    @BiggerThan(0)
    @HumanDescription(value = "рост", format = "целое число > 0")
    private long height; //Значение поля должно быть больше 0
    @BiggerThan(0)
    @HumanDescription(value = "вес", format = "целое число > 0, значение может отсутствовать")
    private Long weight; //Поле может быть null, Значение поля должно быть больше 0
    @HumanDescription(value = "номер паспорта", format = "значение может отсутствовать")
    private String passportID; //Значение этого поля должно быть уникальным, Поле может быть null

    public String getName() {
        return name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public long getHeight() {
        return height;
    }

    public Long getWeight() {
        return weight;
    }

    public String getPassportID() {
        return passportID;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", birthday=" + birthday +
                ", height=" + height +
                ", weight=" + weight +
                ", passportID='" + passportID + '\'' +
                '}';
    }

}
