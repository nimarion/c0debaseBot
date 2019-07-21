package de.c0debase.bot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.vdurmont.emoji.EmojiManager;
import de.c0debase.bot.core.Codebase;
import de.c0debase.bot.database.data.CodebaseUser;
import de.c0debase.bot.utils.EmbedUtils;
import de.c0debase.bot.utils.Pagination;
import de.c0debase.bot.utils.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

/**
 * @author Biosphere
 * @date 23.01.18
 */

public class LeaderboardCommand extends Command {

    private final Codebase bot;

    public LeaderboardCommand(final Codebase instance) {
        this.bot = instance;
        this.name = "leaderboard";
        this.help = "Zeigt dir das Leaderboard";
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        final EmbedBuilder embedBuilder = EmbedUtils.getEmbed(commandEvent.getAuthor(), true);
        embedBuilder.setTitle("Leaderboard: " + commandEvent.getGuild().getName());

        final Pagination pagination = bot.getDataManager().getLeaderboard(commandEvent.getGuild().getId());
        int count = 1;
        for (CodebaseUser codebaseUser : pagination.getPage(1)) {
            final Member member = commandEvent.getGuild().getMemberById(Long.valueOf(codebaseUser.getUserID()));
            if (member != null) {
                embedBuilder.appendDescription("`" + count + ")` " + StringUtils.replaceCharacter(member.getEffectiveName()) + "#" + member.getUser().getDiscriminator() + " (Lvl." + codebaseUser.getLevel() + ")\n");
            } else {
                embedBuilder.appendDescription("`" + count + ")` undefined#0000 (Lvl." + codebaseUser.getLevel() + ")\n");
            }
            count++;
        }

        embedBuilder.setFooter("Seite: (1/" + ((commandEvent.getGuild().getMembers().size() / 10) + 1) + ")", commandEvent.getGuild().getIconUrl());

        commandEvent.getTextChannel().sendMessage(embedBuilder.build()).queue((Message success) -> {
            success.addReaction(EmojiManager.getForAlias("arrow_left").getUnicode()).queue();
            success.addReaction(EmojiManager.getForAlias("arrow_right").getUnicode()).queue();
        });
    }
}
