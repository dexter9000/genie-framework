package com.genie.schedule;

import com.genie.schedule.utils.ClasspathScanner;
import org.apache.commons.lang3.EnumUtils;
import org.junit.Test;

import java.lang.reflect.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

public class GetAndSetTest {

    @Test
    public void testAllDTO() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Set<Class<?>> classes = ClasspathScanner.getClasses("cn.com.connext.crm.service.dto");
        for(Class clazz : classes){
            boolean isAbs = Modifier.isAbstract(clazz.getModifiers());
            if(!clazz.getSimpleName().endsWith("Test") && !isAbs){
                testGetSet(clazz);
            }
        }
    }

    public <T> void testGetSet(Class<T> dtoClass) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        System.out.println("Test get and set for :" + dtoClass.getTypeName());
        T dto = dtoClass.newInstance();

        List<Method> methods = findMethods(dtoClass);

        for (Method method : methods) {
            if (method.getName().startsWith("set")) {

                Class paramClass = method.getParameterTypes()[0];
                String typeName = paramClass.getTypeName();
                if(Modifier.isAbstract(paramClass.getModifiers())){
                    continue;
                }
                if(paramClass.isEnum()){
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
                    case "java.lang.Boolean":
                        method.invoke(dto, Boolean.TRUE);
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
                        method.invoke(dto, 0);
                        break;
                    case "java.lang.Integer":
                    case "java.lang.Long":
                    case "java.lang.Double":
                        method.invoke(dto, 0);
                        break;
                    case "boolean":
                        method.invoke(dto, true);
                        break;
                    default:
                        if(haveConstructor(method.getParameterTypes()[0])){
                            Object obj = method.getParameterTypes()[0].newInstance();
                            method.invoke(dto, obj);
                        }
                        break;
                }
            }else if(method.getName().startsWith("get")){
                method.invoke(dto);
            }
        }

        dto.toString();
    }

    private boolean haveConstructor(Class<?> clazz){
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            if(constructors[0].getParameters().length == 0){
                return true;
            }
        }
        return false;
    }

    private static List<Field> findFields(Class<?> clazz) {
        Class<?> searchType = clazz;
        List<Field> fields = new ArrayList<>();

        while (!Object.class.equals(searchType) && searchType != null) {
            // 这里一次性获取了类中的所有元素
            fields.addAll(Arrays.asList(searchType.getDeclaredFields()));
            // 获取所有父类，接着遍历父类中的元素。
            searchType = searchType.getSuperclass();
        }
        return fields;
    }

    private static List<Method> findMethods(Class<?> clazz) {
        Class<?> searchType = clazz;
        List<Method> methods = new ArrayList<>();

        while (!Object.class.equals(searchType) && searchType != null) {
            // 这里一次性获取了类中的所有元素
            methods.addAll(Arrays.asList(searchType.getDeclaredMethods()));
            // 获取所有父类，接着遍历父类中的元素。
            searchType = searchType.getSuperclass();
        }
        return methods;
    }

}
