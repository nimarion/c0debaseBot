package de.c0debase.bot.database.mongodb;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.json.JsonWriterSettings;

import de.c0debase.bot.database.dao.StarboardDao;
import de.c0debase.bot.database.model.StarboardPost;
import de.c0debase.bot.utils.Constants;

public class StarboardImplMongo implements StarboardDao {

    private final MongoCollection<Document> starboardCollection;
    private final JsonWriterSettings jsonWriterSettings;

    public StarboardImplMongo(final MongoClient mongoClient, final JsonWriterSettings jsonWriterSettings){
        this.starboardCollection = mongoClient.getDatabase("codebase").getCollection("starboard");
        this.jsonWriterSettings = jsonWriterSettings;
    }

    @Override
    public StarboardPost createStarboardPost(StarboardPost starboardPost) {
        starboardCollection.insertOne(Document.parse(Constants.GSON.toJson(starboardPost)));
        return starboardPost;
    }

    @Override
    public StarboardPost getStarboardPost(String messageId) {
        final Document document = starboardCollection.find(Filters.eq("messageId", messageId)).first();
        return document == null ? null : Constants.GSON.fromJson(document.toJson(jsonWriterSettings), StarboardPost.class);
    }

    @Override
    public List<StarboardPost> getStarboardPosts(String guildId, String userId) {
        final List<StarboardPost> starboardPosts = new ArrayList<>();
        starboardCollection.find(Filters.and(Filters.eq("guildId", guildId), Filters.eq("userId", userId))).iterator().forEachRemaining(document -> {
            starboardPosts.add(Constants.GSON.fromJson(document.toJson(jsonWriterSettings), StarboardPost.class));
        });
        return starboardPosts;
    }

    @Override
    public void updateStarboardPost(String messageId, Integer updatedStarCount) {
        final Document document = starboardCollection.find(Filters.eq("messageId", messageId)).first();
        if(document == null){
            throw new NullPointerException();
        }
        document.replace("starCount", updatedStarCount);
        starboardCollection.replaceOne(Filters.eq("messageId", messageId), document);
    }

    @Override
    public void updateStarboardPost(String messageId, String starboardMessageId) {
        final Document document = starboardCollection.find(Filters.eq("messageId", messageId)).first();
        if(document == null){
            throw new NullPointerException();
        }
        document.append("starboardMessageId", starboardMessageId);
        starboardCollection.replaceOne(Filters.eq("messageId", messageId), document);
    }

    @Override
    public void deleteStarboardPost(String messageId) {
        starboardCollection.deleteMany(Filters.eq("messageId", messageId));
    }

}