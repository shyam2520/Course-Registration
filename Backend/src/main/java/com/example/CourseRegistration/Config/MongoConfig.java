package com.example.CourseRegistration.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
// @EnableMongoRepositories(basePackages = "your.package.name")
@EnableMongoRepositories(basePackages  = {"com.example.CourseRegistration.Database"})
public class MongoConfig extends AbstractMongoClientConfiguration {


    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;



    @Override
    protected String getDatabaseName() {
        return "Course_Registration";
    }


    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUri);
    }
}
