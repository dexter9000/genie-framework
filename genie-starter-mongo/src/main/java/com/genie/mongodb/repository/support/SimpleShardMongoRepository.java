package com.genie.mongodb.repository.support;

import com.genie.data.UUIDGenerator;
import com.genie.data.annotation.ShardingId;
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
 */
public class SimpleShardMongoRepository<T, ID> implements ShardMongoRepository<T, ID> {

    private static final Logger log = LoggerFactory.getLogger(SimpleShardMongoRepository.class);

    private final MongoOperations mongoOperations;
    private final MongoEntityInformation<T, ID> entityInformation;

    /**
     * Creates a new {@link org.springframework.data.mongodb.repository.support.SimpleMongoRepository} for the given {@link MongoEntityInformation} and {@link MongoTemplate}.
     *
     * @param metadata        must not be {@literal null}.
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
            entity = UUIDGenerator.generate(entity);
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

            List<S> result = source.stream()
                .map(entity -> UUIDGenerator.generate(entity))
                .collect(Collectors.toList());
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
    public Optional<T> findById(ID id, String shard) {

        Assert.notNull(id, "The given id must not be null!");
        Assert.notNull(shard, "The given shard must not be null!");

        return Optional.ofNullable(
            mongoOperations.findById(id, entityInformation.getJavaType(), getCollectionName(shard)));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#existsById(java.lang.Object)
     */
    @Override
    public boolean existsById(ID id, String shard) {

        Assert.notNull(id, "The given id must not be null!");

        return mongoOperations.exists(getIdQuery(id), entityInformation.getJavaType(),
            getCollectionName(shard));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#count()
     */
    @Override
    public long count(String shard) {
        return mongoOperations.getCollection(getCollectionName(shard)).count();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#deleteById(java.lang.Object)
     */
    @Override
    public void deleteById(ID id, String shard) {

        Assert.notNull(id, "The given id must not be null!");

        mongoOperations.remove(getIdQuery(id), entityInformation.getJavaType(), getCollectionName(shard));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#delete(java.lang.Object)
     */
    @Override
    public void delete(T entity) {

        Assert.notNull(entity, "The given entity must not be null!");

        deleteById(entityInformation.getRequiredId(entity), getShard(entity));
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
    public void deleteAll(String shard) {
        mongoOperations.remove(new Query(), getCollectionName(shard));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#findAll()
     */
    @Override
    public List<T> findAll(String shard) {
        return findAll(new Query(), shard);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#findAllById(java.lang.Iterable)
     */
    @Override
    public Iterable<T> findAllById(Iterable<ID> ids, String shard) {

        return findAll(new Query(new Criteria(entityInformation.getIdAttribute())
            .in(Streamable.of(ids).stream().collect(StreamUtils.toUnmodifiableList()))), shard);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.PagingAndSortingRepository#findAll(org.springframework.data.domain.Pageable)
     */
    @Override
    public Page<T> findAll(String shard, Pageable pageable) {

        Assert.notNull(pageable, "Pageable must not be null!");

        Long count = count(shard);
        List<T> list = findAll(new Query().with(pageable), shard);

        return new PageImpl<>(list, pageable, count);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.PagingAndSortingRepository#findAll(org.springframework.data.domain.Sort)
     */
    @Override
    public List<T> findAll(String shard, Sort sort) {

        Assert.notNull(sort, "Sort must not be null!");

        return findAll(new Query().with(sort), shard);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.mongodb.repository.MongoRepository#insert(java.lang.Object)
     */
    @Override
    public <S extends T> S insert(S entity) {

        Assert.notNull(entity, "Entity must not be null!");

        mongoOperations.insert(UUIDGenerator.generate(entity), getCollectionName(entity));
        return entity;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.mongodb.repository.MongoRepository#insert(java.lang.Iterable)
     */
    @Override
    public <S extends T> List<S> insert(Iterable<S> entities) {

        Assert.notNull(entities, "The given Iterable of entities not be null!");

        List<S> list = Streamable.of(entities).stream()
            .map(entity -> UUIDGenerator.generate(entity))
            .collect(StreamUtils.toUnmodifiableList());

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
        String collectionName = getCollectionName(example.getProbe());
        List<S> list = mongoOperations.find(q, example.getProbeType(), collectionName);

        return PageableExecutionUtils.getPage(list, pageable,
            () -> mongoOperations.count(q, example.getProbeType(), collectionName));
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

        return mongoOperations.find(q, example.getProbeType(), getCollectionName(example.getProbe()));
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
            .ofNullable(mongoOperations.findOne(q, example.getProbeType(), getCollectionName(example.getProbe())));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.query.QueryByExampleExecutor#count(org.springframework.data.domain.Example)
     */
    @Override
    public <S extends T> long count(Example<S> example) {

        Assert.notNull(example, "Sample must not be null!");

        Query q = new Query(new Criteria().alike(example));
        return mongoOperations.count(q, example.getProbeType(), getCollectionName(example.getProbe()));
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

    private List<T> findAll(@Nullable Query query, String shard) {

        if (query == null) {
            return Collections.emptyList();
        }

        return mongoOperations.find(query, entityInformation.getJavaType(), getCollectionName(shard));
    }

    private String getCollectionName(String shardId) {
        // TODO 增加模板格式
        return (shardId == null) ? entityInformation.getCollectionName() : entityInformation.getCollectionName() + shardId;
    }

    private <S extends T> String getCollectionName(S entity) {
        return getCollectionName(getShard(entity));
    }

    private String getShard(T entity) {
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

    @Override
    public List<T> findAll() {
        return null;
    }

    @Override
    public List<T> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(ID id) {
        return false;
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> ids) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(ID id) {

    }

    @Override
    public void deleteAll() {

    }
}
