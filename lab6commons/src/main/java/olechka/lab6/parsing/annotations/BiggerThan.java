package olechka.lab6.parsing.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//спец аннотации которые навешивабтся на аннотации чтобы у аннотаций было представление о том как ими
//можно пользоваться
//объекты аннотаций реализуют интерфейс
//Аннотация @Retention позволяет указать жизненный цикл аннотации:
//будет она присутствовать только в исходном коде,
//в скомпилированном файле, или она будет также видна и в процессе выполнения.
//Аннотация @Target указывает, что именно мы можем пометить этой аннотацией,
// это может быть поле, метод, тип и т.д
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BiggerThan {
    //    это называется параметром аннотации. Параметр описывается, как функция - с двумя скобками
// В качестве параметров могут быть использованы только примитивы,а также String, Enum.
//    то есть нельзя написать List<String> args(); — ошибка.
    long value();
}
