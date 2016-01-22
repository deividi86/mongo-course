package course;

import com.mongodb.BasicDBObject;
import com.mongodb.ErrorCategory;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class BlogPostDAO {
    MongoCollection<Document> postsCollection;

    public BlogPostDAO(final MongoDatabase blogDatabase) {
        postsCollection = blogDatabase.getCollection("posts");
    }

    public Document findByPermalink(String permalink) {
        Document post = postsCollection.find(eq("permalink", permalink)).first();

        if (post == null) {
            System.out.println("post not found");
            return null;
        }

        return post;
    }

    public List<Document> findByDateDescending(int limit) {
        List<Document> posts = postsCollection.find().limit(limit).sort(new BasicDBObject("date",-1)).into(new ArrayList<Document>());
        return posts;
    }

    public String addPost(String title, String body, List tags, String username) {

        System.out.println("inserting blog entry " + title + " " + body);

        String permalink = title.replaceAll("\\s", "_"); // whitespace becomes _
        permalink = permalink.replaceAll("\\W", ""); // get rid of non alphanumeric
        permalink = permalink.toLowerCase();
        permalink = permalink+ (new Date()).getTime();

        Document post = new Document();

        if (username != null && !username.equals("")) {
            post.append("author", username);
        }
        if (title != null && !title.equals("")) {
            post.append("title", title);
        }
        if (body != null && !body.equals("")) {
            post.append("body", body);
        }
        if (permalink != null && !permalink.equals("")) {
            post.append("permalink", permalink);
        }
        if (tags != null && !tags.equals("")) {
            post.append("tags", tags);
        }
        post.append("comments", new ArrayList<String>());
        post.append("date", new Date());

        try {
            postsCollection.insertOne(post);
            return permalink;
        } catch (MongoWriteException e) {
            if (e.getError().getCategory().equals(ErrorCategory.UNCATEGORIZED)) {
                System.out.println("Username already in use: " + username);
                return e.getMessage();
            }
            throw e;
        }
    }

    public void addPostComment(final String name, final String email, final String body,
                               final String permalink) {

        BasicDBObject comment = new BasicDBObject("author",name).append("body", body);
        if(email != null && !email.equals("")){
            comment.append("email",email);
        }
        postsCollection.updateOne(new BasicDBObject("permalink",permalink),new BasicDBObject("$push",new BasicDBObject("comments",comment)));

    }
}
