package com.genie.jpa.repository;

import com.genie.data.UUIDGenerator;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * {@link org.springframework.data.repository.CrudRepository}接口的默认实现. This will offer
 * you a more sophisticated interface than the plain {@link EntityManager} .
 *
 * @param <T> 需要处理的实体类型
 * @param <ID> 实体类型的ID类型
 */
public class JpaRepositoryImpl<T, ID extends Serializable>
    extends SimpleJpaRepository<T, ID> {

    public JpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    public JpaRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
    }

    /**
     * 包含UUID主键生成功能
     * @param entity
     * @param <S>
     * @return
     */
    @Override
    public <S extends T> S save(S entity) {
        entity = UUIDGenerator.generate(entity);
        return super.save(entity);
    }
}
