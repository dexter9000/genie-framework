package com.genie.mongo.util;

import com.genie.mongo.domain.Member;
import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

public class MongodbTest {

    private MongoTemplate template;

    @Before
    public void setup() {
        template = new MongoTemplate(new MongoClient("localhost", 27017), "test");
    }

    @Test
    public void testQuery() {
        long size = 10000;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            Member member = new Member();
            member.setId("ID_" + i);
            member.setName("Name_" + i);
            member.setBirthday("Birthday_" + i);
            template.save(member, "member");
        }
        long endTime = System.currentTimeMillis();

        System.out.println("Save " + size + " members in " + (endTime - startTime) + " ms.");
    }

    @Test
    public void testBulk(){
        BulkOperations bulkOps = template.bulkOps(BulkOperations.BulkMode.UNORDERED, "test");

        Query query = new Query();

        query.addCriteria(Criteria.where("_id").regex("111"));

        Update update = new Update();
        update.set("_id","111");
        update.set("name","222");

        bulkOps.upsert(query, update);

        bulkOps.execute();

        List list = template.findAll(String.class, "test");
        System.out.println(list.size());
    }

}
