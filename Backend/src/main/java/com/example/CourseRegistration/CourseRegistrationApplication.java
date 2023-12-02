package com.example.CourseRegistration;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.RestController;

import com.example.CourseRegistration.Config.MongoConfig;
import com.mongodb.client.MongoClient;


import com.mongodb.client.MongoDatabase;

import org.springframework.web.bind.annotation.GetMapping;


@SpringBootApplication
@org.springframework.context.annotation.ComponentScan(basePackages = {"com.example.CourseRegistration"})
@RestController
// @EnableMongoRepositories(basePackages  = {"com.example.CourseRegistration.Database"})
public class CourseRegistrationApplication {
	
	@Autowired
	MongoConfig mongoConfig = new MongoConfig();

	public static void main(String[] args) {
		SpringApplication.run(CourseRegistrationApplication.class, args);
	}

	@GetMapping("/test")
	public String getMethodName() {
		return "Hello World";
	}

	@GetMapping("/Database")
	public String getDatabase() {
		MongoClient mongoClient = mongoConfig.mongoClient();
		MongoDatabase database = mongoClient.getDatabase("Course_Registration");
	    List<String> collectionNames = database.listCollectionNames().into(new ArrayList<>());

        System.out.println("Collections in the database:");
        for (String collectionName : collectionNames) {
            System.out.println(collectionName);
        }
		return database.getName();
	}

}
