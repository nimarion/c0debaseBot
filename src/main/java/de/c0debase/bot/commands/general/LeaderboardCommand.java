package de.c0debase.bot.commands.general;

import com.vdurmont.emoji.EmojiManager;
import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.commands.Command;
import de.c0debase.bot.database.data.LevelUser;
import de.c0debase.bot.utils.StringUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author Biosphere
 * @date 23.01.18
 */

public class LeaderboardCommand extends Command {

    public LeaderboardCommand() {
        super("leaderboard", "Zeigt dir das Leaderboard", Categorie.GENERAL);
    }

    @Override
    public void execute(String[] args, Message msg) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(msg.getGuild().getSelfMember().getColor());
        embedBuilder.setTitle("Leaderboard: " + msg.getGuild().getName());

        CodebaseBot.getInstance().getMongoDataManager().getLeaderboard(msg.getGuild().getId(), leaderboard -> {
            int count = 1;
            for (LevelUser levelUser : leaderboard.getPage(1)) {
                Member member = msg.getGuild().getMemberById(Long.valueOf(levelUser.getUserID()));
                if (member != null) {
                    embedBuilder.appendDescription("`" + count + ")` " + StringUtils.replaceCharacter(member.getEffectiveName()) + "#" + member.getUser().getDiscriminator() + " (Lvl." + levelUser.getLevel() + ")\n");
                }
                count++;
            }

            embedBuilder.setFooter("Seite: (1/" + ((msg.getGuild().getMembers().size() / 10) + 1) + ")", msg.getGuild().getIconUrl());

            msg.getTextChannel().sendMessage(embedBuilder.build()).queue((Message success) -> {
                success.addReaction(EmojiManager.getForAlias("arrow_left").getUnicode()).queue();
                success.addReaction(EmojiManager.getForAlias("arrow_right").getUnicode()).queue();
            });
        });

    }
}
