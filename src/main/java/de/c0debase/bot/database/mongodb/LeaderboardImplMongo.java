package de.c0debase.bot.database.mongodb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import org.bson.Document;
import org.bson.json.JsonWriterSettings;

import de.c0debase.bot.database.dao.LeaderboardDao;
import de.c0debase.bot.database.model.User;
import de.c0debase.bot.utils.Constants;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

public class LeaderboardImplMongo implements LeaderboardDao {

    private final MongoCollection<Document> userCollection;
    private final JsonWriterSettings jsonWriterSettings;

    private final Map<String, List<User>> leaderboardCache;

    public LeaderboardImplMongo(final MongoClient mongoClient, final JsonWriterSettings jsonWriterSettings) {
        this.userCollection = mongoClient.getDatabase("codebase").getCollection("user");
        this.jsonWriterSettings = jsonWriterSettings;

        leaderboardCache = ExpiringMap.builder().expiration(2, TimeUnit.MINUTES)
                .expirationPolicy(ExpirationPolicy.ACCESSED).build();
    }

    @Override
    public List<User> getLeaderboard(final String guildId) {
        if (leaderboardCache.containsKey(guildId)) {
            return leaderboardCache.get(guildId);
        }
        final List<User> users = new ArrayList<>();
        final FindIterable<Document> documentIterable = userCollection.find(Filters.eq("guildID", guildId))
                .sort(Sorts.descending("level", "xp"));

        for (Document document : documentIterable) {
            users.add(Constants.GSON.fromJson(document.toJson(jsonWriterSettings), User.class));
        }
        users.sort((o1, o2) -> {
            if (o1.getLevel() != o2.getLevel()) {
                return Double.compare(o2.getLevel(), o1.getLevel());
            } else {
                return Double.compare((o2.getXp() + o2.getLevel() + (1000 * o2.getLevel() * 1.2)),
                        (o1.getXp() + o1.getLevel() + (1000 * o1.getLevel() * 1.2)));
            }
        });

        leaderboardCache.put(guildId, users);
        return users;
    }

}