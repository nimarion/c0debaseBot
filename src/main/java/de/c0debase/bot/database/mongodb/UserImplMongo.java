package de.c0debase.bot.database.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.json.JsonWriterSettings;

import de.c0debase.bot.database.dao.UserDao;
import de.c0debase.bot.database.model.User;
import de.c0debase.bot.utils.Constants;

public class UserImplMongo implements UserDao {

    private final MongoCollection<Document> userCollection;
    private final JsonWriterSettings jsonWriterSettings;

    public UserImplMongo(final MongoClient mongoClient, final JsonWriterSettings jsonWriterSettings) {
        this.userCollection = mongoClient.getDatabase("codebase").getCollection("user");
        this.jsonWriterSettings = jsonWriterSettings;
    }

    @Override
    public User getOrCreateUser(String guildId, String userId) {
        User user = getUser(guildId, userId);
        if (user != null) {
            return user;
        }
        return createUser(guildId, userId);
    }

    @Override
    public User getUser(String guildId, String userId) {
        final Document document = userCollection
                .find(Filters.and(Filters.eq("guildID", guildId), Filters.eq("userID", userId))).first();
        if (document == null) {
            return null;
        }
        return Constants.GSON.fromJson(document.toJson(jsonWriterSettings), User.class);
    }

    @Override
    public User createUser(String guildId, String userId) {
        final User user = User.newUser(userId, guildId);
        userCollection.insertOne(Document.parse(Constants.GSON.toJson(user)));
        return user;
    }

    @Override
    public void deleteUser(String guildId, String userId) {
        userCollection.deleteMany(Filters.and(Filters.eq("guildID", guildId), Filters.eq("userID", userId)));
    }

    @Override
    public void updateUser(User user) {
        userCollection.replaceOne(
                Filters.and(Filters.eq("guildID", user.getGuildID()), Filters.eq("userID", user.getUserID())),
                Document.parse(Constants.GSON.toJson(user)));
    }

}