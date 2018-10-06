package com.genie.schedule.service;


import com.mongodb.MongoClient;
import com.novemberain.quartz.mongodb.GenieMongoDBJobStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
public class GenieMongoDBJobStoreTest {


    @Test
    public void scheduleJobs() {
        GenieMongoDBJobStore genieMongoDBJobStore = new GenieMongoDBJobStore();

        GenieMongoDBJobStore mongoClientGenieMongoDBJobStore = new GenieMongoDBJobStore(new MongoClient());

        GenieMongoDBJobStore userNameMongoDBJobStore = new GenieMongoDBJobStore("mongoUri", "username", "password");
    }
}



















