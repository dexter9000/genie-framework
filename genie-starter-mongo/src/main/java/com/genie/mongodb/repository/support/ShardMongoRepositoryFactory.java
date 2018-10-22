package com.genie.mongodb.repository.support;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;

public class ShardMongoRepositoryFactory extends MongoRepositoryFactory {
    /**
     * Creates a new {@link MongoRepositoryFactory} with the given {@link MongoOperations}.
     *
     * @param mongoOperations must not be {@literal null}.
     */
    public ShardMongoRepositoryFactory(MongoOperations mongoOperations) {
        super(mongoOperations);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return ShardMongoRepository.class;
    }
}
