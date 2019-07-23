package de.c0debase.bot.database;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDatabaseManager implements AutoCloseable {

    private static final String DATABASE_NAME = "codebase";
    private static final String USER_COLLECTION_NAME = "user";

    private final MongoClient client;
    private final MongoCollection<Document> users;

    public MongoDatabaseManager(final String host, final int port) {
        client = new MongoClient(new ServerAddress(host, port));
        client.getAddress();
        final MongoDatabase database = client.getDatabase(DATABASE_NAME);
        users = database.getCollection(USER_COLLECTION_NAME);
    }

    @Override
    public void close() {
        client.close();
    }

    public MongoCollection<Document> getUsers() {
        return users;
    }
}