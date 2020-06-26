package de.c0debase.bot.database.dao;

import de.c0debase.bot.database.model.User;

public interface UserDao {

    public User getUser(final String guildId, final String userId);

    public User getOrCreateUser(final String guildId, final String userId);

    public User createUser(final String guildId, final String userId);

    public void deleteUser(final String guildId, final String userId);

    public void updateUser(final User user);
    
}