package de.c0debase.bot.commands.general;

import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.commands.Command;
import de.c0debase.bot.level.LevelUser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class RankCommand extends Command {

    public RankCommand() {
        super("rank", "Zeigt dir deine Level", Categorie.GENERAL, "level");
    }

    @Override
    public void execute(String[] args, Message msg) {
        Member member = msg.getMentionedMembers().isEmpty() ? msg.getMember() : msg.getMentionedMembers().get(0);
        if (member.getUser().isBot()) {
            member = msg.getMember();
        }
        LevelUser levelUser = CodebaseBot.getInstance().getLevelManager().getLevelUser(member.getUser().getId());
        EmbedBuilder embedBuilder = getEmbed(msg.getGuild(), msg.getAuthor());

        embedBuilder.addField("Level", String.valueOf(levelUser.getLevel()), false);
        embedBuilder.addField("Exp", levelUser.getXp() + "/" + (levelUser.getLevel() == 0 ? "1000" : (1000 * levelUser.getLevel() * 1.2)), false);
        if (levelUser.getLevel() == 0 || levelUser.getLevel() == 1) {
            embedBuilder.addField("Total Exp", String.valueOf(levelUser.getLevel() == 0 ? levelUser.getXp() : levelUser.getXp() + 1000), false);
        } else {
            embedBuilder.addField("Total Exp", String.valueOf(((1000 * (levelUser.getLevel() - 1) * 1.2) * levelUser.getLevel()) / 2 + 1000 + levelUser.getXplevelUser.getXp()), false);
        }

        msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }
}

