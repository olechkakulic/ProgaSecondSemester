package olechka.lab5.parsing;

public class CommonHelpers {
    public static <T> T createEnumElement(Class<T> clazz, String str) {
        for (T object : clazz.getEnumConstants()) {
            Enum<?> element = (Enum<?>) object;
            if (element.name().equals(str)) {
                return object;
            }
        }
        throw new RuntimeException("no enum constant with name " + str + " was found");
    }
}
