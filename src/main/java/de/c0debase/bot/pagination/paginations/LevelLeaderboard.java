package de.c0debase.bot.pagination.paginations;

import com.vdurmont.emoji.EmojiManager;

import de.c0debase.bot.database.model.User;
import de.c0debase.bot.pagination.Pagination;
import de.c0debase.bot.utils.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LevelLeaderboard extends Pagination {

    public LevelLeaderboard() {
        super("Level Leaderboard:");
    }

    @Override
    public void update(Message success, MessageEmbed messageEmbed, String emote) {
        int current = getCurrentPage(messageEmbed);
        if (emote.equalsIgnoreCase("arrow_left") && current == 1) {
            return;
        }
        final int max = getMaxPages(messageEmbed);
        final boolean descending = isDescending(messageEmbed);

        if (max != current) {
            if (emote.equalsIgnoreCase("arrow_right")) {
                current++;
            } else if (emote.equalsIgnoreCase("arrow_left") && current > 1) {
                current--;
            }
        } else if (emote.equalsIgnoreCase("arrow_left") && current > 1) {
            current--;
        }

        if (current > 0) {
            final EmbedBuilder embedBuilder = getEmbed(success.getGuild(), current, max, descending);
            buildList(embedBuilder, current, descending, success.getGuild());
            success.editMessage(embedBuilder.build()).queue();
        }
    }

    @Override
    public void createFirst(boolean descending, TextChannel textChannel) {
        final EmbedBuilder embedBuilder = getEmbed(textChannel.getGuild(), descending);

        buildList(embedBuilder, 1, descending, textChannel.getGuild());

        textChannel.sendMessage(embedBuilder.build()).queue((Message success) -> {
            success.addReaction(EmojiManager.getForAlias("arrow_left").getUnicode()).queue();
            success.addReaction(EmojiManager.getForAlias("arrow_right").getUnicode()).queue();
        });
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