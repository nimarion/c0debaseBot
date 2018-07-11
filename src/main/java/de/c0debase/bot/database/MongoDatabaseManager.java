package de.c0debase.bot.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.c0debase.bot.CodebaseBot;
import lombok.Getter;
import org.bson.Document;

/**
 * @author Biosphere
 * @date 27.04.18
 */
@Getter
public class MongoDatabaseManager {

    private static final String DATABASE_NAME = "codebase";
    private static final String USER_COLLECTION_NAME = "user";
    private static final String ACTIVITY_COLLECTION_NAME = "activity";


    private final MongoCollection<Document> users;
    private final MongoCollection<Document> activity;



    public MongoDatabaseManager(final String host, final int port, final String username, final String password) {
        MongoClient mongoClient;
        if (username != null && password != null) {
            mongoClient = new MongoClient(new ServerAddress(host, port),
                    MongoCredential.createCredential(username, DATABASE_NAME, password.toCharArray()),
                    MongoClientOptions.builder().build());
        } else {
            mongoClient = new MongoClient(host, port);
        }
        CodebaseBot.getInstance().getLogger().info("Connected to MongoDB " + mongoClient.getAddress());
        final MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        users = database.getCollection(USER_COLLECTION_NAME);
        activity = database.getCollection(ACTIVITY_COLLECTION_NAME);
    }

}