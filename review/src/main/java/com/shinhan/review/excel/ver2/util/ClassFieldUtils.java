package com.shinhan.review.excel.ver2.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ClassFieldUtils {

    private ClassFieldUtils(){

    }

    public static List<Field> getAllFields(Class<?> clazz){
        List<Field> result = new ArrayList<>();
        List<Class<?>> allClassesIncludingSuperClasses = getAllClassesIncludingSuperClasses(clazz, true);
        for (Class<?> it : allClassesIncludingSuperClasses) {
            result.addAll(Arrays.asList(it.getDeclaredFields()));
        }
        return result;
    }

    public static Annotation getAnnotation(Class<?> clazz, Class<? extends Annotation> target){
        List<Class<?>> allClassesIncludingSuperClasses = getAllClassesIncludingSuperClasses(clazz, true);
        for (Class<?> it : allClassesIncludingSuperClasses) {
            if (it.isAnnotationPresent(target)){
                return it.getAnnotation(target);
            }
        }
        return null;
    }

    public static Field getField(Class<?> clazz, String name) throws Exception{
        List<Class<?>> allClassesIncludingSuperClasses = getAllClassesIncludingSuperClasses(clazz, true);
        for (Class<?> it : allClassesIncludingSuperClasses) {
            for(Field field : it.getDeclaredFields()){
                if (field.getName().equals(name))
                    return it.getDeclaredField(name);
            }
        }
        throw new NoSuchFieldException();
    }

    private static List<Class<?>> getAllClassesIncludingSuperClasses(Class<?> clazz, boolean isSuper){
        List<Class<?>> result = new ArrayList<>();
        while (clazz!=null){
            result.add(clazz);
            clazz = clazz.getSuperclass();
        }
        if (isSuper){
            Collections.reverse(result);
        }
        return result;
    }

}
