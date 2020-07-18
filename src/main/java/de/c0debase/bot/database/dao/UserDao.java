package de.c0debase.bot.database.dao;

import de.c0debase.bot.database.model.User;

public interface UserDao {

    /**
     * Get an User object from the database
     * 
     * @param guildId
     * @param userId
     * @return null if User does not exists
     */
    public User getUser(final String guildId, final String userId);

    /**
     * Get an existing User object from the database or create a new one if it
     * doesnt exists
     * 
     * @param guildId
     * @param userId
     * @return {@link de.c0debase.bot.database.model.User}
     */
    public User getOrCreateUser(final String guildId, final String userId);

    /**
     * Create a new User object in the database
     * 
     * @param guildId
     * @param userId
     * @return {@link de.c0debase.bot.database.model.User}
     */
    public User createUser(final String guildId, final String userId);

    /**
     * Delete a user from the database
     * 
     * @param guildId
     * @param userId
     */
    public void deleteUser(final String guildId, final String userId);

    /**
     * Update the user object in the database
     * 
     * @param user
     */
    public void updateUser(final User user);

}