package com.mongo;

import com.mongodb.*;

import java.net.UnknownHostException;

public class Week2Homework3 {
    public static void main(String[] args) throws UnknownHostException {

        MongoClient client = new MongoClient();
        DB database = client.getDB("students");
        DBCollection collection = database.getCollection("grades");

        QueryBuilder builder = QueryBuilder.start("type").is("homework");

        DBCursor cursor = collection.find(builder.get())
                .sort(new BasicDBObject("student_id", 1).append("score", 1));

        int curStudentId = -1;
        try {
            while (cursor.hasNext()) {
                BasicDBObject doc = (BasicDBObject) cursor.next();
                int studentId = doc.getInt("student_id");

                if (studentId != curStudentId) {
                    collection.remove(doc);
                    curStudentId = studentId;
                }
            }
        } finally {
            cursor.close();
        }
    }
}
