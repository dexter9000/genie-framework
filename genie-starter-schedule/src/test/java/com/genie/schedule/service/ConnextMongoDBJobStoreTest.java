package com.genie.schedule.service;


import com.mongodb.MongoClient;
import com.novemberain.quartz.mongodb.ConnextMongoDBJobStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
public class ConnextMongoDBJobStoreTest {


    @Test
    public void scheduleJobs() {
        ConnextMongoDBJobStore connextMongoDBJobStore = new ConnextMongoDBJobStore();

        ConnextMongoDBJobStore mongoClientConnextMongoDBJobStore = new ConnextMongoDBJobStore(new MongoClient());

        ConnextMongoDBJobStore userNameconnextMongoDBJobStore = new ConnextMongoDBJobStore("mongoUri", "username", "password");
    }
}



















