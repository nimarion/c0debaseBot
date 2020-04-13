package de.c0debase.bot.database;

import de.c0debase.bot.database.data.CodebaseUser;

import java.util.List;

public interface Database extends AutoCloseable {
    
    CodebaseUser getUserData(final String guildID, final String userID);

    void updateUserData(final CodebaseUser codebaseUser);

    List<CodebaseUser> getLeaderboard(final String guildID);

}
