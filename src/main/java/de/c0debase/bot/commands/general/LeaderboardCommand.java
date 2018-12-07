package de.c0debase.bot.commands.general;

import com.vdurmont.emoji.EmojiManager;
import de.c0debase.bot.commands.Command;
import de.c0debase.bot.core.Codebase;
import de.c0debase.bot.database.data.CodebaseUser;
import de.c0debase.bot.utils.Pagination;
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
        super("leaderboard", "Zeigt dir das Leaderboard", Category.GENERAL);
    }

    @Override
    public void execute(final Codebase bot, final String[] args, final Message message) {
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(message.getGuild().getSelfMember().getColor());
        embedBuilder.setTitle("Leaderboard: " + message.getGuild().getName());

        final Pagination pagination = bot.getDataManager().getLeaderboard(message.getGuild().getId());
        int count = 1;
        for (CodebaseUser codebaseUser : pagination.getPage(1)) {
            final Member member = message.getGuild().getMemberById(Long.valueOf(codebaseUser.getUserID()));
            if (member != null) {
                embedBuilder.appendDescription("`" + count + ")` " + StringUtils.replaceCharacter(member.getEffectiveName()) + "#" + member.getUser().getDiscriminator() + " (Lvl." + codebaseUser.getLevel() + ")\n");
            }
            count++;
        }

        embedBuilder.setFooter("Seite: (1/" + ((message.getGuild().getMembers().size() / 10) + 1) + ")", message.getGuild().getIconUrl());

        message.getTextChannel().sendMessage(embedBuilder.build()).queue((Message success) -> {
            success.addReaction(EmojiManager.getForAlias("arrow_left").getUnicode()).queue();
            success.addReaction(EmojiManager.getForAlias("arrow_right").getUnicode()).queue();
        });
    }
}
