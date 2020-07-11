package de.c0debase.bot.database;

import de.c0debase.bot.database.dao.LeaderboardDao;
import de.c0debase.bot.database.dao.StarboardDao;
import de.c0debase.bot.database.dao.UserDao;

public interface Database extends AutoCloseable{
    
    UserDao getUserDao();

    LeaderboardDao getLeaderboardDao();

    StarboardDao getStarboardDao();

}
