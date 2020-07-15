package de.c0debase.bot.pagination.paginations;

import de.c0debase.bot.database.model.User;
import de.c0debase.bot.pagination.Pagination;
import de.c0debase.bot.utils.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LevelLeaderboard extends Pagination {

    public LevelLeaderboard() {
        super("Level Leaderboard:");
    }

    @Override
    public void buildList(EmbedBuilder embedBuilder, int page, boolean descending, Guild guild) {
        final List<User> users = getBot().getDatabase().getLeaderboardDao().getLeaderboard(guild.getId());
        if (!descending) {
            Collections.reverse(users);
        }
        for (Map.Entry<Integer, User> entry : getPage(page, users, descending).entrySet()) {
            User user = entry.getValue();
            int count = entry.getKey();
            final Member member = guild.getMemberById(Long.valueOf(user.getUserID()));
            if (member != null) {
                embedBuilder
                        .appendDescription("`" + count + ")` " + StringUtils.replaceCharacter(member.getEffectiveName())
                                + "#" + member.getUser().getDiscriminator() + " (Lvl." + user.getLevel() + "/Xp."
                                + user.getXp() + ")\n");
            } else {
                embedBuilder.appendDescription(
                        "`" + count + ")` undefined#0000 (Lvl." + user.getLevel() + "/Xp." + user.getXp() + ")\n");
            }
            count++;
        }
    }
}