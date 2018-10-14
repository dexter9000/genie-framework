package com.genie.mongodb.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * 类的描述：
 * <p>自定义MongoDB Config </p>
 * Spring 此处文件配置初始化 MongoDB 配置、自定义MongoDB Factory
 */
@Configuration
@EnableAutoConfiguration
public class MongoConfig {
    private final MongoClientOptions options;
    private final MongoClientFactory factory;
    private MongoClient mongo;

    /**
     * * 功能描述: 构造函数  直接使用MongoAutoConfiguration构造函数 参数初始化由Spring完成
     *
     * @param properties  MongoDB 配置数据
     * @param options     TODO: 待完善 断点跟踪Spring框架传参进来是null
     * @param environment TODO: 待完善 断点跟踪Spring框架传参进来是null
     */
    public MongoConfig(MongoProperties properties, ObjectProvider<MongoClientOptions> options, Environment environment) {
        this.options = (MongoClientOptions) options.getIfAvailable();
        this.factory = new MongoClientFactory(properties, environment);
    }

    /**
     * * 功能描述: 覆盖默认的MongoDbFactory
     */
    @Bean
    MultiTenantMongoDbFactory mongoDbFactory() {
        this.mongo = this.factory.createMongoClient(this.options);
        MultiTenantMongoDbFactory mongoDbFactory = new MultiTenantMongoDbFactory(mongo, "pvQC_dev");
        return mongoDbFactory;
    }

    /**
     * * 功能描述: Create MongoTemplate
     */
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }
}
