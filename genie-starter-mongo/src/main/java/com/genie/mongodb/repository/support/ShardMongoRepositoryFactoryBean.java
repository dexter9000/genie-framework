package com.genie.mongodb.repository.support;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import java.io.Serializable;

public class ShardMongoRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
    extends MongoRepositoryFactoryBean<T, S, ID> {

    /**
     * Creates a new {@link MongoRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public ShardMongoRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport getFactoryInstance(MongoOperations operations) {
        return new ShardMongoRepositoryFactory(operations);
    }
}
