package com.mongo;

/**
 * Created by Deividi on 17/02/2016.
 */
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Question8 {
	public static void main(String[] args) {
		MongoClient c =  new MongoClient();
		MongoDatabase db = c.getDatabase("test");
		MongoCollection<Document> animals = db.getCollection("animals");

		Document animal = new Document("animal", "monkey");
		animals.insertOne(animal);
		animal.remove("animal");
		animal.append("animal", "cat");
		animals.insertOne(animal);
		animal.remove("animal");
		animal.append("animal", "lion");
		animals.insertOne(animal);

		System.out.println(animals.count());
	}
}
