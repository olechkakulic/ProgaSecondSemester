package olechka.lab5.parsing;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;

public class JsonObjectParser {

    public static JSONObject jsonParser(Object object) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (object == null) {
                return null;
            }
            Class<?> clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(object);
                if (value != null && (value.getClass().isPrimitive() || value.getClass().isEnum() || Number.class.isAssignableFrom(value.getClass()) || value.getClass() == Boolean.class || value.getClass() == String.class || Temporal.class.isAssignableFrom(value.getClass()))) {
                    jsonObject.put(field.getName(), value);
                } else {
                    jsonObject.put(field.getName(), jsonParser(value));
                }

            }
            return jsonObject;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T createFromJsonObject(Class<T> clazz, JSONObject jsonObject) throws ReflectiveOperationException {
        T object = clazz.getConstructor().newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = jsonObject.opt(field.getName());
                Class<?> type = field.getType();
                if (value instanceof JSONObject) {
                    Object fieldValue = createFromJsonObject(type, (JSONObject) value);
                    field.set(object, fieldValue);
                } else if (value instanceof String && type.isEnum()) {
                    Object enumValue = CommonHelpers.createEnumElement(type, (String) value);
                    field.set(object, enumValue);
                } else if (value instanceof String && type == LocalDate.class) {
                    LocalDate date = LocalDate.parse((String) value);
                    field.set(object, date);
                } else if (value instanceof String && type == LocalDateTime.class) {
                    LocalDateTime date = LocalDateTime.parse((String) value);
                    field.set(object, date);
                } else if (value instanceof Number) {
                    Number number = (Number) value;
                    if (type == double.class || type == Double.class) {
                        field.set(object, number.doubleValue());
                    } else if (type == Long.class || type == long.class) {
                        field.set(object, number.longValue());
                    } else {
                        field.set(object, number);
                    }
                } else {
                    field.set(object, value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return object;
    }
}
