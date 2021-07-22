package Util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectUtils {

	public static Object invoke(Class<?> sourceClazz, String methodName, Object instance) {
		try {
			Method method = sourceClazz.getDeclaredMethod(methodName);
			method.setAccessible(true);
			return method.invoke(instance, null);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}

	}

	public static <T> T getFieldValue(Field field, Object instance, Class<T> clazz) {
		field.setAccessible(true);
		try {
			return clazz.cast(field.get(instance));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T getFieldValue(Class<?> sourceClazz, String fieldName, Object instance, Class<T> clazz) {
		try {
			Field field = sourceClazz.getDeclaredField(fieldName);
			return getFieldValue(field, instance, clazz);
		} catch (NoSuchFieldException | SecurityException e) {
			System.out.println("以下のフィールドのみ対応しています。");
			Arrays.stream(sourceClazz.getDeclaredFields()).forEach(System.out::println);
			throw new RuntimeException(e);
		}
	}

	public static Object getFieldValue(Class<?> sourceClazz, String fieldName, Object instance) {
		return getFieldValue(sourceClazz, fieldName, instance, Object.class);
	}

	public static Object getFieldValue(Field field, Object instance) {
		return getFieldValue(field, instance, Object.class);
	}
}
