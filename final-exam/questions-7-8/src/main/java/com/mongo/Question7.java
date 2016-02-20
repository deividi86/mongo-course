package com.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.all;

import static com.mongodb.client.model.Sorts.ascending;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by deividi.silva on 17/02/2016.
 */
public class Question7 {
    public static void main(String[] args) throws IOException {
        MongoClient c =  new MongoClient();
        MongoDatabase db = c.getDatabase("photos");
        MongoCollection<Document> images = db.getCollection("images");
        MongoCollection<Document> albums = db.getCollection("albums");
        MongoCursor<Document> cursor = images.find().sort(ascending("_id")).iterator();

        try {
            while(cursor.hasNext()){
                Document image = cursor.next();
                Integer imageId = (Integer) image.get("_id");
                Document album = albums.find().filter(eq("images",imageId)).first();
                if(album == null){
                    images.findOneAndDelete(image);
                }
            }
        } finally {
            cursor.close();
        }

        System.out.println("Result: " +images.count(all("tags", Arrays.asList("sunrises"))));
    }
}
