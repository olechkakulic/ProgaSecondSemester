package olechka.lab7.models;

import olechka.lab7.parsing.annotations.BiggerThan;
import olechka.lab7.parsing.annotations.HumanDescription;
import olechka.lab7.parsing.annotations.NonNull;

import java.io.Serializable;

public class Coordinates implements Serializable {
    @BiggerThan(-47)
    @HumanDescription(format = "целое число > -47")
    @NonNull
    private Integer x; //Значение поля должно быть больше -47
    @NonNull
    @HumanDescription(format = "дробное число")
    private double y; //Поле не может быть null

    public Integer getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
