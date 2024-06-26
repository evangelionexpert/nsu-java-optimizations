package javaopt;

import java.lang.Class;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class Util {
    public static int strlen(String s) {
        return s.length();
    }

    // pass Object... args to allow methods with args
    public static Object invokeMethod(Object o, String methodName) throws Exception {
        return o.getClass().getMethod(methodName).invoke(o);
    }

    // для примитивов есть свои методы (setInt, setByte итд)
    public static void changeField(Object o, String fieldName, Object fieldValue) throws Exception {
        o.getClass().getField(fieldName).set(o, fieldValue);
    }
}
