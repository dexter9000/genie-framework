package com.genie.mongodb.repository.support;

import com.genie.data.annotation.ShardingId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class ShardUtil {
    private static final Logger log = LoggerFactory.getLogger(ShardUtil.class);

    public static <T> String getShard(T entity) {
        for (Field field : entity.getClass().getDeclaredFields()) {
            ShardingId id = field.getAnnotation(ShardingId.class);
            if (id != null) {
                try {
                    field.setAccessible(true);
                    String shardValue = field.get(entity).toString();
                    field.setAccessible(false);
                    return shardValue;
                } catch (IllegalAccessException e) {
                    // TODO
                    log.error("error", e);
                }
            }
        }
        return "";
    }

}
