package de.c0debase.bot.database;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import de.c0debase.bot.core.Codebase;
import de.c0debase.bot.database.data.CodebaseUser;
import de.c0debase.bot.utils.Constants;
import de.c0debase.bot.utils.Pagination;
import net.jodah.expiringmap.ExpiringMap;
import org.bson.Document;
import org.bson.json.JsonWriterSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Biosphere
 * @date 27.04.18
 */
public class MongoDatabase implements Database {

    //Cache Map
    private final Map<String, CodebaseUser> userCache;
    private final Map<String, Pagination> leaderboardCache;
   
    //MongoDB
    private final MongoClient mongoClient;
    private final MongoCollection<Document> userCollection;

    private final JsonWriterSettings jsonWriterSettings;
    private final Codebase bot;

    public MongoDatabase(final String host, final int port, final Codebase bot) {
        this.bot = bot;

        jsonWriterSettings = JsonWriterSettings.builder()
                .int64Converter((value, writer) -> writer.writeNumber(value.toString()))
                .build();

        //Connect to MongoDB server
        mongoClient = new MongoClient(new ServerAddress(host, port));
        userCollection = mongoClient.getDatabase("codebase").getCollection("user");
        
        //Initialize ExpiringMap
        final ExpiringMap.Builder<Object, Object> mapBuilder = ExpiringMap.builder();
        mapBuilder.maxSize(123).expiration(1, TimeUnit.MINUTES).build();
        leaderboardCache = mapBuilder.build();
        userCache = mapBuilder.build();
    }

    
    /**
     *
     * @param guildID The id of a guild
     * @param userID The id of a user
     * @return A {@link CodebaseUser}
     */
    public CodebaseUser getUserData(final String guildID, final String userID) {
        if (userCache.containsKey(guildID + "-" + userID)) {
            return userCache.get(guildID + "-" + userID);
        }
        final Document document = getUserCollection().find(Filters.and(Filters.eq("guildID", guildID), Filters.eq("userID", userID))).first();
        final CodebaseUser codebaseUser;
        if (document == null) {
            codebaseUser = new CodebaseUser();
            codebaseUser.setGuildID(guildID);
            codebaseUser.setLevel(0);
            codebaseUser.setCoins(0.0);
            codebaseUser.addXP(50);
            codebaseUser.setCoins(codebaseUser.getXp() * 0.05);
            codebaseUser.setLastMessage(System.currentTimeMillis());
            codebaseUser.setUserID(userID);
            codebaseUser.setRoles(new ArrayList<>());
            getUserCollection().insertOne(Document.parse(Constants.GSON.toJson(codebaseUser)));
        } else {
            codebaseUser = Constants.GSON.fromJson(document.toJson(jsonWriterSettings), CodebaseUser.class);
            if (codebaseUser.getCoins() == null) {
                codebaseUser.setCoins(codebaseUser.getXp() * 0.01);
            }
        }
        userCache.put(guildID + "-" + userID, codebaseUser);
        return codebaseUser;
    }

    /**
     *
     * @param codebaseUser The {@link CodebaseUser} to be updated
     */
    public void updateUserData(final CodebaseUser codebaseUser) {
        getUserCollection().replaceOne(Filters.and(Filters.eq("guildID", codebaseUser.getGuildID()), Filters.eq("userID", codebaseUser.getUserID())), Document.parse(Constants.GSON.toJson(codebaseUser)));
        userCache.put(codebaseUser.getGuildID() + "-" + codebaseUser.getUserID(), codebaseUser);
    }

    /**
     * Get a sorted {@link Pagination} of a guild
     * @param guildID The id of a guild
     * @return A sorted {@link Pagination} of the requested guild
     */
    public Pagination getLeaderboard(final String guildID) {
        if (leaderboardCache.containsKey(guildID)) {
            return leaderboardCache.get(guildID);
        }
        final List<CodebaseUser> codebaseUsers = new ArrayList<>();
        final FindIterable<Document> document = getUserCollection()
                .find(Filters.eq("guildID", guildID))
                .sort(Sorts.descending("level", "xp"));

        for (Document aDocument : document) {
            if (bot.getJDA().getUserById(aDocument.getString("userID")) != null) {
                codebaseUsers.add(Constants.GSON.fromJson(aDocument.toJson(jsonWriterSettings), CodebaseUser.class));
            }
        }
        codebaseUsers.sort((o1, o2) -> {
            if (o1.getLevel() != o2.getLevel()) {
                return Double.compare(o2.getLevel(), o1.getLevel());
            } else {
                return Double.compare((o2.getXp() + o2.getLevel() + (1000 * o2.getLevel() * 1.2)), (o1.getXp() + o1.getLevel() + (1000 * o1.getLevel() * 1.2)));
            }
        });

        final Pagination pagination = new Pagination(codebaseUsers, 10);
        leaderboardCache.put(guildID, pagination);
        return pagination;
    }

    private MongoCollection<Document> getUserCollection(){
        return userCollection;
    }

    @Override
    public void close() throws Exception {
        mongoClient.close();
    }

}
