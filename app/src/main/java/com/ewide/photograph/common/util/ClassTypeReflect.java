package com.ewide.photograph.common.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * author：Taozebi
 * date：2018/11/12 19:05
 * describe：
 */

public class ClassTypeReflect {
    public static Type getModelClazz(Class<?> subclass) {
        return getGenericType(0, subclass);
    }

    private static Type getGenericType(int index, Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (!(superclass instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) superclass).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            throw new RuntimeException("Index outof bounds");
        }

        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return params[index];
    }
}
