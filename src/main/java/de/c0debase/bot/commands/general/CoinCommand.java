package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

public class CoinCommand extends Command {

    public CoinCommand() {
        super("coins", "Zeigt dir deine Coins", Category.GENERAL, "money");
    }

    @Override
    public void execute(final String[] args, final Message message) {
        final Member member = message.getMentionedMembers().size() == 0 ? message.getMember() : ((message.getMentionedMembers().get(0).getUser().isBot()) ? message.getMember() : message.getMentionedMembers().get(0));
        final EmbedBuilder embedBuilder = getEmbed(message.getGuild(), member.getUser());
        embedBuilder.setDescription(member.getAsMention() + " hat " + String.format("%.2f", bot.getDataManager().getUserData(member.getGuild().getId(), member.getUser().getId()).getCoins()) + " Coins");
        message.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }
}
