package de.c0debase.bot.database.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import org.bson.json.JsonWriterSettings;

import de.c0debase.bot.database.Database;
import de.c0debase.bot.database.dao.LeaderboardDao;
import de.c0debase.bot.database.dao.UserDao;

public class MongoDatabase implements Database {

    private final MongoClient mongoClient;

    // DAOs
    private final UserDao userDao;
    private final LeaderboardDao leaderboardDao;

    public MongoDatabase(final String host, final int port) {
        mongoClient = new MongoClient(new ServerAddress(host, port));
        final JsonWriterSettings jsonWriterSettings = JsonWriterSettings.builder()
                .int64Converter((value, writer) -> writer.writeNumber(value.toString())).build();

        // Init DAOs
        userDao = new UserImplMongo(mongoClient, jsonWriterSettings);
        leaderboardDao = new LeaderboardImplMongo(mongoClient, jsonWriterSettings);
    }

    @Override
    public UserDao getUserDao() {
        return userDao;
    }

    @Override
    public LeaderboardDao getLeaderboardDao() {
        return leaderboardDao;
    }

    @Override
    public void close() throws Exception {
        mongoClient.close();
    }

}