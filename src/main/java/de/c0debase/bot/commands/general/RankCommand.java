package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.database.model.User;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class RankCommand extends Command {

    public RankCommand() {
        super("rank", "Zeigt dir deine Level", Category.GENERAL, "level");
    }

    @Override
    public void execute(final String[] args, final Message message) {
        final Member member = message.getMentionedMembers().size() == 0 ? message.getMember() : ((message.getMentionedMembers().get(0).getUser().isBot()) ? message.getMember() : message.getMentionedMembers().get(0));
        final EmbedBuilder embedBuilder = getEmbed(message.getGuild(), member.getUser());
        final User user = bot.getDatabase().getUserDao().getOrCreateUser(member.getGuild().getId(), member.getUser().getId());

        embedBuilder.addField("Level", String.valueOf(user.getLevel()), false);
        embedBuilder.addField("Exp", user.getXp() + "/" + (user.getLevel() == 0 ? "1000" : (1000 * user.getLevel() * 1.2)), false);
        if (user.getLevel() == 0 || user.getLevel() == 1) {
            embedBuilder.addField("Total Exp", String.valueOf(user.getLevel() == 0 ? user.getXp() : user.getXp() + 1000), false);
        } else {
            embedBuilder.addField("Total Exp", String.valueOf(Double.valueOf(((1000 * (user.getLevel() - 1) * 1.2) * user.getLevel()) / 2 + 1000 + user.getXp())), false);
        }

        message.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }
}

