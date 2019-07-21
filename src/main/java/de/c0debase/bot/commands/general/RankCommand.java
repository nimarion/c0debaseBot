package de.c0debase.bot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import de.c0debase.bot.core.Codebase;
import de.c0debase.bot.database.data.CodebaseUser;
import de.c0debase.bot.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class RankCommand extends Command {

    private final Codebase bot;

    public RankCommand(final Codebase instance) {
        this.bot = instance;
        this.name = "level";
        this.help = "Zeigt dir deine Level";
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        final Message message = commandEvent.getMessage();
        final Member member = message.getMentionedMembers().size() == 0 ? message.getMember() : ((message.getMentionedMembers().get(0).getUser().isBot()) ? message.getMember() : message.getMentionedMembers().get(0));
        final EmbedBuilder embedBuilder = EmbedUtils.getEmbed(commandEvent.getAuthor(), true);
        final CodebaseUser codebaseUser = bot.getDataManager().getUserData(member.getGuild().getId(), member.getUser().getId());

        embedBuilder.addField("Level", String.valueOf(codebaseUser.getLevel()), false);
        embedBuilder.addField("Exp", codebaseUser.getXp() + "/" + (codebaseUser.getLevel() == 0 ? "1000" : (1000 * codebaseUser.getLevel() * 1.2)), false);
        if (codebaseUser.getLevel() == 0 || codebaseUser.getLevel() == 1) {
            embedBuilder.addField("Total Exp", String.valueOf(codebaseUser.getLevel() == 0 ? codebaseUser.getXp() : codebaseUser.getXp() + 1000), false);
        } else {
            embedBuilder.addField("Total Exp", String.valueOf(Double.valueOf(((1000 * (codebaseUser.getLevel() - 1) * 1.2) * codebaseUser.getLevel()) / 2 + 1000 + codebaseUser.getXp())), false);
        }
        commandEvent.reply(embedBuilder.build());
    }
}

