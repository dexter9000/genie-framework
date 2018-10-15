package com.genie.mongodb.repository.support;

import com.genie.mongodb.annotation.ShardId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.data.util.StreamUtils;
import org.springframework.data.util.Streamable;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Repository base implementation for Mongo.
 *
 * @author Oliver Gierke
 * @author Christoph Strobl
 * @author Thomas Darimont
 * @author Mark Paluch
 */
public class SimpleShardMongoRepository<T, ID, SHARD> implements ShardMongoRepository<T, ID, SHARD> {

    private static final Logger log = LoggerFactory.getLogger(SimpleShardMongoRepository.class);

    private final MongoOperations mongoOperations;
    private final MongoEntityInformation<T, ID> entityInformation;

    /**
     * Creates a new {@link org.springframework.data.mongodb.repository.support.SimpleMongoRepository} for the given {@link MongoEntityInformation} and {@link MongoTemplate}.
     *
     * @param metadata must not be {@literal null}.
     * @param mongoOperations must not be {@literal null}.
     */
    public SimpleShardMongoRepository(MongoEntityInformation<T, ID> metadata, MongoOperations mongoOperations) {

        Assert.notNull(metadata, "MongoEntityInformation must not be null!");
        Assert.notNull(mongoOperations, "MongoOperations must not be null!");

        this.entityInformation = metadata;
        this.mongoOperations = mongoOperations;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#save(java.lang.Object)
     */
    @Override
    public <S extends T> S save(S entity) {

        Assert.notNull(entity, "Entity must not be null!");

        if (entityInformation.isNew(entity)) {
            mongoOperations.insert(entity, getCollectionName(entity));
        } else {
            mongoOperations.save(entity, getCollectionName(entity));
        }

        return entity;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.mongodb.repository.MongoRepository#saveAll(java.lang.Iterable)
     */
    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {

        Assert.notNull(entities, "The given Iterable of entities not be null!");

        Streamable<S> source = Streamable.of(entities);
        boolean allNew = source.stream().allMatch(it -> entityInformation.isNew(it));

        if (allNew) {

            List<S> result = source.stream().collect(Collectors.toList());
            mongoOperations.insert(result, getCollectionName(result.get(0)));
            return result;

        } else {
            return source.stream().map(this::save).collect(Collectors.toList());
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#findById(java.io.Serializable)
     */
    @Override
    public Optional<T> findById(ID id, SHARD shard) {

        Assert.notNull(id, "The given id must not be null!");

        return Optional.ofNullable(
            mongoOperations.findById(id, entityInformation.getJavaType(), entityInformation.getCollectionName()));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#existsById(java.lang.Object)
     */
    @Override
    public boolean existsById(ID id, SHARD shard) {

        Assert.notNull(id, "The given id must not be null!");

        return mongoOperations.exists(getIdQuery(id), entityInformation.getJavaType(),
            entityInformation.getCollectionName());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#count()
     */
    @Override
    public long count(SHARD shard) {
        return mongoOperations.getCollection(entityInformation.getCollectionName()).count();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#deleteById(java.lang.Object)
     */
    @Override
    public void deleteById(ID id, SHARD shard) {

        Assert.notNull(id, "The given id must not be null!");

        mongoOperations.remove(getIdQuery(id), entityInformation.getJavaType(), entityInformation.getCollectionName());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#delete(java.lang.Object)
     */
    @Override
    public void delete(T entity) {

        Assert.notNull(entity, "The given entity must not be null!");

        deleteById(entityInformation.getRequiredId(entity), getShard());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#delete(java.lang.Iterable)
     */
    @Override
    public void deleteAll(Iterable<? extends T> entities) {

        Assert.notNull(entities, "The given Iterable of entities not be null!");

        entities.forEach(this::delete);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#deleteAll()
     */
    @Override
    public void deleteAll(SHARD shard) {
        mongoOperations.remove(new Query(), entityInformation.getCollectionName());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#findAll()
     */
    @Override
    public List<T> findAll(SHARD shard) {
        return findAll(new Query());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#findAllById(java.lang.Iterable)
     */
    @Override
    public Iterable<T> findAllById(Iterable<ID> ids, SHARD shard) {

        return findAll(new Query(new Criteria(entityInformation.getIdAttribute())
            .in(Streamable.of(ids).stream().collect(StreamUtils.toUnmodifiableList()))));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.PagingAndSortingRepository#findAll(org.springframework.data.domain.Pageable)
     */
    @Override
    public Page<T> findAll(SHARD shard, Pageable pageable) {

        Assert.notNull(pageable, "Pageable must not be null!");

        Long count = count(shard);
        List<T> list = findAll(new Query().with(pageable));

        return new PageImpl<T>(list, pageable, count);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.PagingAndSortingRepository#findAll(org.springframework.data.domain.Sort)
     */
    @Override
    public List<T> findAll(SHARD shard, Sort sort) {

        Assert.notNull(sort, "Sort must not be null!");

        return findAll(new Query().with(sort));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.mongodb.repository.MongoRepository#insert(java.lang.Object)
     */
    @Override
    public <S extends T> S insert(S entity) {

        Assert.notNull(entity, "Entity must not be null!");

        mongoOperations.insert(entity, entityInformation.getCollectionName());
        return entity;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.mongodb.repository.MongoRepository#insert(java.lang.Iterable)
     */
    @Override
    public <S extends T> List<S> insert(Iterable<S> entities) {

        Assert.notNull(entities, "The given Iterable of entities not be null!");

        List<S> list = Streamable.of(entities).stream().collect(StreamUtils.toUnmodifiableList());

        if (list.isEmpty()) {
            return list;
        }

        mongoOperations.insertAll(list);
        return list;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.mongodb.repository.MongoRepository#findAllByExample(org.springframework.data.domain.Example, org.springframework.data.domain.Pageable)
     */
    @Override
    public <S extends T> Page<S> findAll(final Example<S> example, Pageable pageable) {

        Assert.notNull(example, "Sample must not be null!");
        Assert.notNull(pageable, "Pageable must not be null!");

        Query q = new Query(new Criteria().alike(example)).with(pageable);
        List<S> list = mongoOperations.find(q, example.getProbeType(), entityInformation.getCollectionName());

        return PageableExecutionUtils.getPage(list, pageable,
            () -> mongoOperations.count(q, example.getProbeType(), entityInformation.getCollectionName()));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.mongodb.repository.MongoRepository#findAllByExample(org.springframework.data.domain.Example, org.springframework.data.domain.Sort)
     */
    @Override
    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {

        Assert.notNull(example, "Sample must not be null!");
        Assert.notNull(sort, "Sort must not be null!");

        Query q = new Query(new Criteria().alike(example)).with(sort);

        return mongoOperations.find(q, example.getProbeType(), entityInformation.getCollectionName());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.mongodb.repository.MongoRepository#findAllByExample(org.springframework.data.domain.Example)
     */
    @Override
    public <S extends T> List<S> findAll(Example<S> example) {
        return findAll(example, Sort.unsorted());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.query.QueryByExampleExecutor#findOne(org.springframework.data.domain.Example)
     */
    @Override
    public <S extends T> Optional<S> findOne(Example<S> example) {

        Assert.notNull(example, "Sample must not be null!");

        Query q = new Query(new Criteria().alike(example));
        return Optional
            .ofNullable(mongoOperations.findOne(q, example.getProbeType(), entityInformation.getCollectionName()));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.query.QueryByExampleExecutor#count(org.springframework.data.domain.Example)
     */
    @Override
    public <S extends T> long count(Example<S> example) {

        Assert.notNull(example, "Sample must not be null!");

        Query q = new Query(new Criteria().alike(example));
        return mongoOperations.count(q, example.getProbeType(), entityInformation.getCollectionName());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.query.QueryByExampleExecutor#exists(org.springframework.data.domain.Example)
     */
    @Override
    public <S extends T> boolean exists(Example<S> example) {

        Assert.notNull(example, "Sample must not be null!");

        Query q = new Query(new Criteria().alike(example));
        return mongoOperations.exists(q, example.getProbeType(), entityInformation.getCollectionName());
    }

    private Query getIdQuery(Object id) {
        return new Query(getIdCriteria(id));
    }

    private Criteria getIdCriteria(Object id) {
        return where(entityInformation.getIdAttribute()).is(id);
    }

    private List<T> findAll(@Nullable Query query) {

        if (query == null) {
            return Collections.emptyList();
        }

        return mongoOperations.find(query, entityInformation.getJavaType(), entityInformation.getCollectionName());
    }

    private String getCollectionName(String shardId){
        return entityInformation.getCollectionName() + shardId;
    }

    private <S extends T> String getCollectionName(S entity){
        for (Field field : entity.getClass().getDeclaredFields()) {
            ShardId id = field.getAnnotation(ShardId.class);
            if (id != null) {
                try {
                    field.setAccessible(true);
                    String indexName = entityInformation.getCollectionName() + field.get(entity);
                    field.setAccessible(false);
                    return indexName;
                } catch (IllegalAccessException e) {
                    // TODO
                    log.error("error", e);
                }
            }
        }
        return entityInformation.getCollectionName();
    }

    private SHARD getShard(){
        return null;
    }
}
