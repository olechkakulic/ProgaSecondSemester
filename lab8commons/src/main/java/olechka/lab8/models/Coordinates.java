package olechka.lab8.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import olechka.lab8.parsing.annotations.BiggerThan;
import olechka.lab8.parsing.annotations.HumanDescription;
import olechka.lab8.parsing.annotations.NonNull;

import java.io.Serializable;

@ToString
@Getter
@Setter
public class Coordinates implements Serializable {
    @BiggerThan(-47)
    @HumanDescription(format = "целое число > -47")
    @NonNull
    private Integer x; //Значение поля должно быть больше -47
    @NonNull
    @HumanDescription(format = "дробное число")
    private double y; //Поле не может быть null
}
