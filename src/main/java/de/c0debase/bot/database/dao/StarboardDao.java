package de.c0debase.bot.database.dao;

import java.util.List;

import de.c0debase.bot.database.model.StarboardPost;

public interface StarboardDao {

    StarboardPost createStarboardPost(StarboardPost starboardPost);

    StarboardPost getStarboardPost(String messageId);

    List<StarboardPost> getStarboardPosts(String guildId, String userId);

    void updateStarboardPost(String messageId, Integer updatedStarCount);

    void updateStarboardPost(String messageId, String starboardMessageId);

    void deleteStarboardPost(String messageId);
    
}