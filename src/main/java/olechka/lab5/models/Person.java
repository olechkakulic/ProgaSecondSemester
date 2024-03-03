package olechka.lab5.models;

import olechka.lab5.parsing.annotations.BiggerThan;
import olechka.lab5.parsing.annotations.HumanDescription;
import olechka.lab5.parsing.annotations.NonNull;

import java.time.LocalDateTime;

public class Person {
    @NonNull
    @HumanDescription(value = "имя", format = "непустая строка")
    private String name; //Поле не может быть null, Строка не может быть пустой
    @HumanDescription(value = "дату рождения", format = "строка вида 00:00:00 00:00:00, значение может отсутствовать")
    private LocalDateTime birthday; //Поле может быть null
    @BiggerThan(0)
    @HumanDescription(value = "рост", format = "целое число > 0")
    private int height; //Значение поля должно быть больше 0
    @BiggerThan(0)
    @HumanDescription(value = "вес", format = "целое число > 0, значение может отсутствовать")
    private Integer weight; //Поле может быть null, Значение поля должно быть больше 0
    @HumanDescription(value = "номер паспорта", format = "значение может отсутствовать")
    private String passportID; //Значение этого поля должно быть уникальным, Поле может быть null

    public String getName() {
        return name;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public int getHeight() {
        return height;
    }

    public Integer getWeight() {
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
