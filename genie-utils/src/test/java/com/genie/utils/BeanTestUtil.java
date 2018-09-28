package com.genie.utils;

import org.apache.commons.lang3.EnumUtils;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class BeanTestUtil {

    public <T> void testGetSet(Class<T> beanClass) throws IllegalAccessException, InstantiationException {
        System.out.println("Test get and set for :" + beanClass.getTypeName());
        T dto = beanClass.newInstance();

        Method[] methods = beanClass.getDeclaredMethods();

        for (Method method : methods) {
            try {
                if (method.getName().startsWith("set")) {

                    Class paramClass = method.getParameterTypes()[0];
                    String typeName = paramClass.getTypeName();
                    if (paramClass.isEnum()) {
                        method.invoke(dto, EnumUtils.getEnumList(paramClass).get(0));
                        continue;
                    }
                    switch (typeName) {
                        case "java.lang.String":
                            method.invoke(dto, "ABC");
                            break;
                        case "java.util.Date":
                            method.invoke(dto, new Date());
                            break;
                        case "java.time.LocalDate":
                            method.invoke(dto, LocalDate.now());
                            break;
                        case "java.time.ZonedDateTime":
                            method.invoke(dto, ZonedDateTime.now());
                            break;
                        case "java.util.List":
                            method.invoke(dto, Arrays.asList());
                            break;
                        case "java.util.Set":
                            method.invoke(dto, new HashSet());
                            break;
                        case "java.util.Map":
                            method.invoke(dto, new HashMap());
                            break;
                        case "int":
                        case "long":
                        case "double":
                        case "java.lang.Integer":
                        case "java.lang.Long":
                        case "java.lang.Double":
                            method.invoke(dto, 0);
                            break;
                        case "boolean":
                        case "java.lang.Boolean":
                            method.invoke(dto, true);
                            break;
                        default:
                            Object obj = method.getParameterTypes()[0].newInstance();
                            method.invoke(dto, obj);
                            break;
                    }
                } else if (method.getName().startsWith("get")) {
                    method.invoke(dto);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
