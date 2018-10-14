package com.genie.mongodb.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.util.Assert;

import java.util.HashMap;

/**
 * <p>自定义MongoDB Factory </p>
 * 自定义MongoDb 配置工厂，可实现多租户切换访问。
 */
public class MultiTenantMongoDbFactory extends SimpleMongoDbFactory {
    private static final Logger logger = LoggerFactory.getLogger(MultiTenantMongoDbFactory.class);
    /**
     * 默认数据库名称
     **/
    private final String defaultName;
    /**
     * MongoDB模板类
     **/
    private MongoTemplate mongoTemplate;
    /**
     * 用户所在线程使用数据库集合
     **/
    private static final ThreadLocal<String> dbName = new ThreadLocal<>();

    private static final HashMap<String, Object> databaseIndexMap = new HashMap<>();

    public MultiTenantMongoDbFactory(final MongoClient mongo, final String defaultDatabaseName) {
        super(mongo, defaultDatabaseName);
        logger.debug("Instantiating " + MultiTenantMongoDbFactory.class.getName() + " with default database name: " + defaultDatabaseName);
        this.defaultName = defaultDatabaseName;
    }

    /**
     * * 功能描述:  dirty but ... what can I do?
     * @param
     */
    public void setMongoTemplate(final MongoTemplate mongoTemplate) {
        Assert.isNull(this.mongoTemplate, "You can set MongoTemplate just once");
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * * 功能描述: 将databaseName放入dbName
     *
     * @param databaseName database/scheme名称
     */
    public static void setDatabaseNameForCurrentThread(final String databaseName) {
        logger.debug("Switching to database: " + databaseName);
        dbName.set(databaseName);
    }

    /**
     * * 功能描述: 清空dbName
     */
    public static void clearDatabaseNameForCurrentThread() {
        if (logger.isDebugEnabled()) {
            logger.debug("Removing database [" + dbName.get() + "]");
        }
        dbName.remove();
    }

    @Override
    public MongoDatabase getDb() {
        final String tlName = dbName.get();
        final String dbToUse = (tlName != null ? tlName : this.defaultName);
        logger.debug("Acquiring database: " + dbToUse);
        createIndexIfNecessaryFor(dbToUse);
        return super.getDb(dbToUse);
    }

    /**
     * 功能描述:
     * TODO: 没搞明白作用
     *
     * @param database database/scheme 名称
     */
    private void createIndexIfNecessaryFor(final String database) {
        if (this.mongoTemplate == null) {
            logger.error("MongoTemplate is null, will not create any index.");
            return;
        }//        sync and init once
        boolean needsToBeCreated = false;
        synchronized (MultiTenantMongoDbFactory.class) {
            final Object syncObj = databaseIndexMap.get(database);
            if (syncObj == null) {
                databaseIndexMap.put(database, new Object());
                needsToBeCreated = true;
            }
        }//        make sure only one thread enters with needsToBeCreated = true
        synchronized (databaseIndexMap.get(database)) {
            if (needsToBeCreated) {
                logger.debug("Creating indices for database name=[" + database + "]");
                createIndexes();
                logger.debug("Done with creating indices for database name=[" + database + "]");
            }
        }
    }

    private void createIndexes() {
        final MongoMappingContext mappingContext = (MongoMappingContext) this.mongoTemplate.getConverter().getMappingContext();
        final MongoPersistentEntityIndexResolver indexResolver = new MongoPersistentEntityIndexResolver(mappingContext);
        for (BasicMongoPersistentEntity<?> persistentEntity : mappingContext.getPersistentEntities()) {
            checkForAndCreateIndexes(indexResolver, persistentEntity);
        }
    }

    private void checkForAndCreateIndexes(final MongoPersistentEntityIndexResolver indexResolver, final MongoPersistentEntity<?> entity) {
        //        make sure its a root document
        if (entity.findAnnotation(Document.class) != null) {
            for (MongoPersistentEntityIndexResolver.IndexDefinitionHolder indexDefinitionHolder : indexResolver.resolveIndexFor(entity.getTypeInformation())) {
                //                work because of javas reentered lock feature
                this.mongoTemplate.indexOps(entity.getType()).ensureIndex(indexDefinitionHolder);
            }
        }
    }
}
