package olechka.lab5.parsing;


import olechka.lab5.interaction.Console;
import olechka.lab5.interaction.InterationClosedException;
import olechka.lab5.parsing.annotations.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Класс для того, чтобы парсить объекты с помощью рефлексии, выводить объекты.
 */
public class ObjectParser {
    //    Function<> - Общепринятая фигня для лямбд
    //    у нас есть HashMap в который мы в качестве ключа будем сувать объект класса Class,
    //    а в качестве значения будет лежать сама лямбда функция, которая принимает объект типа Scanner и возврващает то, что
    ////    мы считали, а точнее объект функциального интерфейса.
    //    так как нам не очень известен тип Class<>, (тут в <> может быть что угодно: и Integer, и String и т.д.), поэтому
    //    мы пишем ?
    //    каждая лямбда функция является объектом функционального интерфейса
    /**
     * Статическое поле класса, в виде HashMap, в котором в качестве ключа хранятся объекты класса Class,
     * в качестве значения - объект функционального интерфейса.
     */
    public static Map<Class<?>, Function<String, ?>> factories = new HashMap<>();

    /**
     * Статический инициализатор для инициализации поля класса factories
     * Здесь лямбда-функция, в качестве параметра принимает строку, а затем превращает ее в нужный класс.
     */
    static {
        factories.put(String.class, String::valueOf);
        factories.put(Double.class, (string) -> Double.parseDouble(string.replace(",", ".")));
        factories.put(Integer.class, Integer::parseInt);
        factories.put(Float.class, Float::parseFloat);
        factories.put(Byte.class, Byte::parseByte);
        factories.put(Short.class, Short::parseShort);
        factories.put(Boolean.class, ObjectParser::parseHumanBoolean);
        factories.put(Long.class, Long::parseLong);
        factories.put(Character.class, (string) -> string.charAt(0));

        factories.put(double.class, (string) -> Double.parseDouble(string.replace(",", ".")));
        factories.put(int.class, Integer::parseInt);
        factories.put(float.class, Float::parseFloat);
        factories.put(byte.class, Byte::parseByte);
        factories.put(short.class, Short::parseShort);
        factories.put(boolean.class, ObjectParser::parseHumanBoolean);
        factories.put(long.class, Long::parseLong);
        factories.put(char.class, (string) -> string.charAt(0));

        factories.put(LocalDate.class, (string) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            return LocalDate.parse(string, formatter);
        });
        factories.put(LocalDateTime.class, (string) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
            return LocalDateTime.parse(string, formatter);
        });
    }

    /**
     * Функция создания объекта класса (для моей лабы - объекта класса Studygroup)
     *
     * @param clazz     - объект класса Class, который передается в метод get() для получения по ключу HashMap-а factories объекта функционального интерфейса
     * @param console   - объект класса Console, необходимый для считывания информации с консоли
     * @param allowNull
     * @return (T) function.apply(string) - return лямбда-функции в зависимости от переданного объекта класса Class
     */
    @SuppressWarnings("unchecked")
    public static <T> T createInteractive(Class<T> clazz, Console console, boolean allowNull) {
        Function<String, ?> function = factories.get(clazz);
        if (function != null) {
            String string = console.nextLine();
            if (string.isEmpty()) {
                if (clazz.isPrimitive()) {
                    throw new NullPointerException();
                }
                return null;
            }
            return (T) function.apply(string);
        }
        //        если мы передаем Енам, то с помощью рефлексии получаем константы Enum-a и выводим, для того чтобы пользователь
        //        мог их увидеть (запрос по лабе)
        if (clazz.isEnum()) {
            console.getOut().print(Arrays.toString(clazz.getEnumConstants()) + ": ");
            //            вызов отдельной функции для работы с енамами
            return createEnumInteractive(clazz, console);
        }

        try {
            return createClassInstanceInteractive(clazz, console, allowNull);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T createClassInstanceInteractive(Class<T> clazz, Console console, boolean allowNull) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (allowNull) {
            console.getOut().print("Хотите ввести данные объекта? (да/нет) ");
//            считываем ответ из сканера.
            Boolean answer = createInteractive(boolean.class, console, false);
            if (answer == null || answer == false) {
//                если вернется null то он запишется в поле, если вернется false то тоже
                return null;
            }
        }
        //        если же все-таки function вернула null, значит это объект неизвестного нам класса
        //        опять же с помощью рефлексии создадим объект этого класса и получим поля
        T object = clazz.getConstructor().newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            //            если над полем висит такая аннотация, то надо игнорировать взаимодействие с таким полем
            //            т.к. по условию лабы оно будет заполняться автоматически
            if (field.isAnnotationPresent(IgnoreInput.class)) {
                continue;
            }
            //            получаем человекоподобный вид названия полей класса
            String humanName = getHumanFieldName(field);
//            получаем формат, который увидит пользователь при вводе
            String format = getHumanFormat(field);
            if (format.isEmpty()) {
                console.getOut().print("Введите " + humanName + ": ");
            } else {
                console.getOut().print("Введите " + humanName + " (" + format + ") " + ": ");
            }
            boolean fieldAllowNull = !field.isAnnotationPresent(NonNull.class);
            //            для приватных полей все разрешаем.
            field.setAccessible(true);
            //            нам надо получить тип полей. Любой класс наследуется от Object,
            //            поэтому пишем именно Object value
            Object value;
            //            цикл do-while как минимум исполнится один раз. необходимо это условие, потому что иначе, переменная value не будет инициализирована.
            do {
                try {
                    //                    рекурсивный вызов функции, получаем тип поля и уже дальше епта раскладываем по полочкам. упиваюсь гениальностью.
                    value = createInteractive(field.getType(), console, fieldAllowNull);
                    //                    совершается проверка на валидность
                    validate(field, value);
                    break;
                } catch (InterationClosedException e) {
                    throw e;
                } catch (Exception exception) {
                    console.getOut().print("Введите корректные данные: ");
                }
            }
            while (true);
            //            для поля объекта этого класса устанавливается значение которое мы присвоили
            field.set(object, value);
        }
        return object;
    }

    /**
     * Функция для того шобы печатать объект на экран. Используется для команды show
     *
     * @param object
     * @return out - строка, которая содержит все поля объекта и их значения
     */
    public static String printObject(Object object) {
        if (object == null) {
            return "null";
        }
        Class<?> clazz = object.getClass();
        //        проверка на наследование от Number-a, покрываем обертки над примитивными
        if (clazz.isPrimitive() || clazz.isEnum() || Number.class.isAssignableFrom(clazz) || clazz == Boolean.class || clazz == String.class || Temporal.class.isAssignableFrom(clazz)) {
            return String.valueOf(object);
        }
        Field[] fields = clazz.getDeclaredFields();
        String out = "";
        for (Field field : fields) {
            String humanName = getHumanFieldName(field);
            try {
                field.setAccessible(true);
//                в функцию get необходимо передавать именно объект. в цикле будут по очереди заполняться value
                Object value = field.get(object);
                out += humanName + ": " + printObject(value) + "\n";
            } catch (ReflectiveOperationException e) {
                out += humanName + ": не удалось получить информацию из-за ошибки " + e;
            }
        }
        return out;

    }

    /**
     * Функция для того чтобы получать понятные для простого смертного названия полей
     *
     * @param field - поле объекта
     * @return field.getName() - строка, которая содержит все поля объекта и их значения
     */
    public static String getHumanFieldName(Field field) {
        //        если над полем висит такая аннотация, то мы получаем объект аннотации, а затем ее значение.
        if (field.isAnnotationPresent(HumanDescription.class)) {
            HumanDescription description = field.getAnnotation(HumanDescription.class);
            if (description.value().isEmpty()) {
                return field.getName();
            }
            return description.value();
        }
        return field.getName();
    }

    public static String getHumanFormat(Field field) {
        if (field.isAnnotationPresent(HumanDescription.class)) {
            HumanDescription description = field.getAnnotation(HumanDescription.class);
            return description.format();
        }
        return "";
    }

    /**
     * Отдельный метод для того чтобы создавать объекты енамов
     *
     * @param clazz - объект класса Class
     * @return object - объект енама
     */
    private static <T> T createEnumInteractive(Class<T> clazz, Console console) {
        String str = console.nextLine();
        String upperCase = str.toUpperCase();
        if (upperCase.isEmpty()) {
            return null;
        }
        return CommonHelpers.createEnumElement(clazz, upperCase);
    }

    /**
     * Метод для проверки на валидность данных. Организовано все при помощи аннотаций.
     *
     * @param field - поле объекта
     * @param value - значение объекта
     * @return object - объект енама
     */
    private static void validate(Field field, Object value) {
        if (value == null && field.isAnnotationPresent(NonNull.class)) {
            throw new NullPointerException();
        }
        if (value != null && field.isAnnotationPresent(BiggerThan.class)) {
            BiggerThan annotation = field.getAnnotation(BiggerThan.class);
            Number number = (Number) value;
            if (number.longValue() <= annotation.value()) {
                throw new IllegalArgumentException();
            }
        }
        if (value != null && field.isAnnotationPresent(SmallerThan.class)) {
            SmallerThan annotation = field.getAnnotation(SmallerThan.class);
            Number number = (Number) value;
            if (number.longValue() >= annotation.value()) {
                throw new IllegalArgumentException();
            }
        }
    }

    private static boolean parseHumanBoolean(String str) {
        switch (str.toLowerCase()) {
            case "да", "yes", "true" -> {
                return true;
            }
            case "нет", "no", "false" -> {
                return false;
            }
            default -> {
                throw new IllegalArgumentException();
            }
        }
    }
}
