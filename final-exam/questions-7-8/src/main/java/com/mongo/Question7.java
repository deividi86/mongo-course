package com.mongo;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
/**
 * Created by deividi.silva on 17/02/2016.
 */
public class Question7 {
    public static void main(String[] args) throws IOException {
        // db.albums.ensureIndex({"images" : 1})
        MongoClient c =  new MongoClient();
        MongoDatabase db = c.getDatabase("photo");
        MongoCollection<Document> images = db.getCollection("images");
        MongoCollection<Document> albums = db.getCollection("albums");

        long totalCount = cursor.count();
        long processedCount = 0;
        long removedCount = 0;
        try {
            while(cursor.hasNext()) {
                DBObject image = cursor.next();
                Integer imageId = (Integer) image.get("_id");
                query = new BasicDBObject("images", imageId);
                DBObject album = albums.findOne(query);
                if (album == null) {
                    images.remove(image);
                    ++removedCount;

                }
                ++processedCount;
            }
        } finally {
            cursor.close();
        }

    }
}
