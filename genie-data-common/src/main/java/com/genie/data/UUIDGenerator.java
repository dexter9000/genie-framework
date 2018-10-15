package com.genie.data;

import java.util.UUID;

/**
 * UUID生成器，对于实现了{@link UUIDDomain}接口的实体类可以在持久化前自动生成UUID
 */
public class UUIDGenerator {

    public static <S> S generate(S entity) {
        if (entity instanceof UUIDDomain) {
            UUIDDomain domain = (UUIDDomain) entity;
            if (domain.getId() == null || "".equals(domain.getId())) {
                String id = UUID.randomUUID().toString();
                domain.setId(id);
            }
        }
        return entity;
    }
}
