package de.c0debase.bot.database.dao;

import java.util.List;

import de.c0debase.bot.database.model.User;

public interface LeaderboardDao {

    List<User> getLeaderboard(String guildId);
    
}