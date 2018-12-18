package com.genie.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class EnumUtil {
    private static final Logger log = LoggerFactory.getLogger(EnumUtil.class);

    public static <T extends ValueEnum> T getEnum(Class<T> clazz, Object value){
        try {
            Method method = clazz.getMethod("values");
            ValueEnum[] inter = (ValueEnum[]) method.invoke(null, null);
            for(ValueEnum v : inter){
                if(v.getValue().equals(value)){
                    return (T) v;
                }
            }
        } catch (Exception e) {
            log.error("Enum {} not include value : {}", clazz.getName(), value);
        }

        return null;
    }
}
