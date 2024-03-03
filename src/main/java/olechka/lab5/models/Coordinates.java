package olechka.lab5.models;

import olechka.lab5.parsing.annotations.BiggerThan;
import olechka.lab5.parsing.annotations.HumanDescription;
import olechka.lab5.parsing.annotations.NonNull;

public class Coordinates {
    @BiggerThan(-47)
    @HumanDescription(format = "целое число > -47")
    private double x; //Значение поля должно быть больше -47
    @NonNull
    @HumanDescription(format = "целое число")
    private Double y; //Поле не может быть null

    public double getX() {
        return x;
    }

    public Double getY() {
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
