package de.c0debase.bot.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
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
public class MongoDataManager implements DataManager {

    private final MongoDatabaseManager databaseManager;
    private final Map<String, CodebaseUser> userCache;
    private final Map<String, Pagination> leaderboardCache;
    private final JsonWriterSettings jsonWriterSettings;

    public MongoDataManager(final String host, final int port) {
        jsonWriterSettings = JsonWriterSettings.builder()
                .int64Converter((value, writer) -> writer.writeNumber(value.toString()))
                .build();
        databaseManager = new MongoDatabaseManager(host, port);
        final ExpiringMap.Builder<Object, Object> mapBuilder = ExpiringMap.builder();
        mapBuilder.maxSize(123).expiration(1, TimeUnit.MINUTES).build();
        leaderboardCache = mapBuilder.build();
        userCache = mapBuilder.build();
    }


    public CodebaseUser getUserData(final String guildID, final String userID) {
        if (userCache.containsKey(guildID + "-" + userID)) {
            return userCache.get(guildID + "-" + userID);
        }
        final Document document = databaseManager.getUsers().find(Filters.and(Filters.eq("guildID", guildID), Filters.eq("userID", userID))).first();
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
            databaseManager.getUsers().insertOne(Document.parse(Constants.GSON.toJson(codebaseUser)));
        } else {
            codebaseUser = Constants.GSON.fromJson(document.toJson(jsonWriterSettings), CodebaseUser.class);
            if (codebaseUser.getCoins() == null) {
                codebaseUser.setCoins(codebaseUser.getXp() * 0.01);
            }
        }
        userCache.put(guildID + "-" + userID, codebaseUser);
        return codebaseUser;
    }

    public void updateUserData(final CodebaseUser codebaseUser) {
        databaseManager.getUsers().replaceOne(Filters.and(Filters.eq("guildID", codebaseUser.getGuildID()), Filters.eq("userID", codebaseUser.getUserID())), Document.parse(Constants.GSON.toJson(codebaseUser)));
        userCache.put(codebaseUser.getGuildID() + "-" + codebaseUser.getUserID(), codebaseUser);
    }

    public Pagination getLeaderboard(final String guildID) {
        if (leaderboardCache.containsKey(guildID)) {
            return leaderboardCache.get(guildID);
        }
        final List<CodebaseUser> codebaseUsers = new ArrayList<>();
        final FindIterable<Document> document = databaseManager.getUsers()
                .find(Filters.eq("guildID", guildID))
                .sort(Sorts.descending("level", "xp"));

        for (Document aDocument : document) {
            codebaseUsers.add(Constants.GSON.fromJson(aDocument.toJson(jsonWriterSettings), CodebaseUser.class));
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

    @Override
    public void close() {
        databaseManager.close();
    }
}
