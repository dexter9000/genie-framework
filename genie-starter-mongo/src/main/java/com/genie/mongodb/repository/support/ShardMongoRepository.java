package com.genie.mongodb.repository.support;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;
import java.util.Optional;

public interface ShardMongoRepository<T, ID> extends MongoRepository<T, ID>, QueryByExampleExecutor<T>  {

    <S extends T> S save(S entity);

    <S extends T> List<S> saveAll(Iterable<S> entities);

    Optional<T> findById(ID id, String shard);

    boolean existsById(ID id, String shard);

    long count(String shard);

    void deleteById(ID id, String shard);

    void delete(T entity);

    void deleteAll(Iterable<? extends T> entities);

    void deleteAll(String shard);

    List<T> findAll(String shard);

    Iterable<T> findAllById(Iterable<ID> ids, String shard);

    Page<T> findAll(String shard, Pageable pageable);

    List<T> findAll(String shard, Sort sort);

    <S extends T> S insert(S entity);

    <S extends T> List<S> insert(Iterable<S> entities);


}
