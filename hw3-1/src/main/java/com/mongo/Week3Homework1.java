package com.mongo;

import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class Week3Homework1 {
    public static void main(String[] args) throws UnknownHostException {

        MongoClient client = new MongoClient();

        DB database = client.getDB("school");
        DBCollection collection = database.getCollection("students");

        DBObject query = new BasicDBObject("scores.type", "homework");
        DBCursor c = collection.find(query);

        while (c.hasNext()) {
            double lowest = 100;

            DBObject student = c.next();
            ArrayList<BasicDBObject> scores = (ArrayList) student.get("scores");

            for (BasicDBObject dbo : scores) {
                if (dbo.getString("type").equals("homework")) {
                    double score = dbo.getDouble("score");
                    if (score < lowest) {
                        lowest = score;
                    }
                }
            }

            DBObject match = new BasicDBObject("_id", student.get("_id"));
            DBObject update = new BasicDBObject("scores", new BasicDBObject("type", "homework").append("score", lowest));
            collection.update(match, new BasicDBObject("$pull", update));
        }
    }
}
