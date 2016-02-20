package com.mongo;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.net.UnknownHostException;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.ascending;

public class Week2Homework3 {
    public static void main(String[] args) throws UnknownHostException {

        MongoClient client = new MongoClient();
        MongoDatabase database = client.getDatabase("students");
        MongoCollection grades = database.getCollection("grades");

        QueryBuilder builder = QueryBuilder.start("type").is("homework");

        MongoCursor cursor = grades.find().filter(eq("type","homework")).sort(ascending("student_id","score")).iterator();

        int curStudentId = -1;
        try {
            while (cursor.hasNext()) {
                Document doc = (Document) cursor.next();
                int studentId = (Integer) doc.get("student_id");

                if (studentId != curStudentId) {
                    grades.deleteOne(doc);
                    curStudentId = studentId;
                }
            }
        } finally {
            cursor.close();
        }
    }
}
