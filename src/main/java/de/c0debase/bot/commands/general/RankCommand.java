package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.database.data.CodebaseUser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

public class RankCommand extends Command {

    public RankCommand() {
        super("rank", "Zeigt dir deine Level", Category.GENERAL, "level");
    }

    @Override
    public void execute(final String[] args, final Message message) {
        final Member member = message.getMentionedMembers().size() == 0 ? message.getMember() : ((message.getMentionedMembers().get(0).getUser().isBot()) ? message.getMember() : message.getMentionedMembers().get(0));
        final EmbedBuilder embedBuilder = getEmbed(message.getGuild(), member.getUser());
        final CodebaseUser codebaseUser = bot.getDataManager().getUserData(member.getGuild().getId(), member.getUser().getId());

        embedBuilder.addField("Level", String.valueOf(codebaseUser.getLevel()), false);
        embedBuilder.addField("Exp", codebaseUser.getXp() + "/" + (codebaseUser.getLevel() == 0 ? "1000" : (1000 * codebaseUser.getLevel() * 1.2)), false);
        if (codebaseUser.getLevel() == 0 || codebaseUser.getLevel() == 1) {
            embedBuilder.addField("Total Exp", String.valueOf(codebaseUser.getLevel() == 0 ? codebaseUser.getXp() : codebaseUser.getXp() + 1000), false);
        } else {
            embedBuilder.addField("Total Exp", String.valueOf(Double.valueOf(((1000 * (codebaseUser.getLevel() - 1) * 1.2) * codebaseUser.getLevel()) / 2 + 1000 + codebaseUser.getXp())), false);
        }

        message.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }
}

