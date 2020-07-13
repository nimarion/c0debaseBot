package de.c0debase.bot.database.dao;

import java.util.List;

import de.c0debase.bot.database.model.User;

public interface LeaderboardDao {

    /**
     * 
     * @param guildId
     * @return A List with all User objects in the database sorted by level and xp
     */
    List<User> getLeaderboard(String guildId);

}