package com.genie.data;

import java.util.UUID;

/**
 * Created by meng013 on 2017/8/12.
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
