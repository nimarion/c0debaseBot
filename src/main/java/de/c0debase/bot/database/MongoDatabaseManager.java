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
public class MongoDatabaseManager {

    private static final String DATABASE_NAME = "codebase";
    private static final String LEVEL_COLLECTION_NAME = "levels";


    private final MongoClient mongoClient;
    @Getter
    private final MongoCollection<Document> levels;

    public MongoDatabaseManager(final String host, final int port, final String username, final String password) {
        if (username != null && password != null) {
            mongoClient = new MongoClient(new ServerAddress(host, port),
                    MongoCredential.createCredential(username, DATABASE_NAME, password.toCharArray()),
                    MongoClientOptions.builder().build());
        } else {
            mongoClient = new MongoClient(host, port);
        }
        CodebaseBot.getInstance().getLogger().info("Connected to MongoDB " + mongoClient.getAddress());
        final MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        levels = database.getCollection(LEVEL_COLLECTION_NAME);

    }

}
