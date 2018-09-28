package com.novemberain.quartz.mongodb;


import com.mongodb.MongoClient;
import org.quartz.JobPersistenceException;
import org.quartz.SchedulerConfigException;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.SchedulerSignaler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class ConnextMongoDBJobStore extends MongoDBJobStore {

    private final Logger log = LoggerFactory.getLogger(ConnextMongoDBJobStore.class);

    public ConnextMongoDBJobStore() {
    }

    public ConnextMongoDBJobStore(MongoClient mongo) {
        this.mongo = mongo;
    }

    public ConnextMongoDBJobStore(String mongoUri, String username, String password) {
        this.mongoUri = mongoUri;
        this.username = username;
        this.password = password;
    }

    @Override
    protected ClassLoadHelper getClassLoaderHelper(ClassLoadHelper original) {
        return original;
    }

    @Override
    public void initialize(ClassLoadHelper loadHelper, SchedulerSignaler signaler) throws SchedulerConfigException {
        try {
            Field field = getClass().getSuperclass().getDeclaredField("assembler");
            field.setAccessible(true);
            MongoStoreAssembler assembler = (MongoStoreAssembler) field.get(this);
            assembler.build(this, loadHelper, signaler);
            if (this.isClustered()) {
                assembler.triggerRecoverer.recover();
                assembler.checkinExecutor.start();
            }
        } catch (JobPersistenceException e) {
            log.error("Cannot recover triggers!", e);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("ConnextMongoDBJobStore initialize error!", e);
        }
    }

}
