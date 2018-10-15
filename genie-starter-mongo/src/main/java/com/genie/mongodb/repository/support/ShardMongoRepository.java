package com.genie.mongodb.repository.support;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;
import java.util.Optional;

public interface ShardMongoRepository<T, ID, SHARD> extends QueryByExampleExecutor<T>  {

    <S extends T> S save(S entity);

    <S extends T> List<S> saveAll(Iterable<S> entities);

    Optional<T> findById(ID id, SHARD shard);

    boolean existsById(ID id, SHARD shard);

    long count(SHARD shard);

    void deleteById(ID id, SHARD shard);

    void delete(T entity);

    void deleteAll(Iterable<? extends T> entities);

    void deleteAll(SHARD shard);

    List<T> findAll(SHARD shard);

    Iterable<T> findAllById(Iterable<ID> ids, SHARD shard);

    Page<T> findAll(SHARD shard, Pageable pageable);

    List<T> findAll(SHARD shard, Sort sort);

    <S extends T> S insert(S entity);

    <S extends T> List<S> insert(Iterable<S> entities);


}
