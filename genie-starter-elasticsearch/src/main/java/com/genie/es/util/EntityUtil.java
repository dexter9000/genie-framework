package com.genie.es.util;

import com.genie.es.annotation.ShardingId;
import com.genie.es.annotation.ESIndex;
import com.genie.es.exception.ElasticSearchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

/**
 * 类处理工具
 */
public class EntityUtil {
    private static final Logger log = LoggerFactory.getLogger(EntityUtil.class);

    private EntityUtil(){
        // 隐藏构造方法
    }

    /**
     * 查询当前类的第一个泛型类型
     *
     * @param repositoryClass 基础类
     * @return 泛型类型
     */
    public static Class getGenericClass(Class repositoryClass) {
        Class tClass = (Class) ((ParameterizedType) repositoryClass.getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }

    /**
     * 根据实体类的{@link ESIndex}注解查询索引名
     *
     * @param entity 实体对象
     * @param <T>    泛型类型
     * @return 索引名
     */
    public static <T> String getIndex(T entity) {
        ESIndex index = entity.getClass().getAnnotation(ESIndex.class);
        if(index.shard()){
            return getShardingName(entity);
        }

        if (index == null) {
            throw new ElasticSearchException("Elastic Search Entity is null : " + entity.getClass().getTypeName());
        }
        return index.name();
    }

    /**
     * 根据实体类的{@link ESIndex}注解查询索引名
     *
     * @param entityClass 实体对象类
     * @param <T>         泛型类型
     * @return 索引名
     */
    public static <T> String getIndex(Class<T> entityClass) {
        ESIndex index = entityClass.getAnnotation(ESIndex.class);
        if(index.shard()){
            throw new ElasticSearchException("Can't get shardingName by Class.");
        }
        if (index == null) {
            throw new ElasticSearchException("Elastic Search Entity is null : " + entityClass.getTypeName());
        }
        return index.name();
    }


    public static <T> String getShardingName(T entity) {
        ESIndex ESIndex = entity.getClass().getAnnotation(ESIndex.class);
        // TODO 需要修改index
        for (Field field : entity.getClass().getDeclaredFields()) {
            ShardingId id = field.getAnnotation(ShardingId.class);
            if (id != null) {
                try {
                    field.setAccessible(true);
                    String indexName = ESIndex.name() + field.get(entity);
                    field.setAccessible(false);
                    return indexName;
                } catch (IllegalAccessException e) {
                    // TODO
                    log.error("error", e);
                }
            }
        }
        return ESIndex.value();
    }

    /**
     * 根据{@link ShardingId}注解查询分表下标字段
     *
     * @param entity 实体对象
     * @param <T>    泛型类型
     * @return 下标字段
     */
    public static <T> String getShardingId(T entity) {
        try {
            for (Field field : entity.getClass().getDeclaredFields()) {
                if (field.getAnnotation(ShardingId.class) != null) {
                    field.setAccessible(true);
                    String result = (String) field.get(entity);
                    field.setAccessible(false);
                    return result;
                }
            }
            return "";
        } catch (Exception e) {
            throw new ElasticSearchException("Get Filed Value error!", e);
        }
    }

    public static <T> String getType(T entity) {
        return entity.getClass().getSimpleName();
    }
}
