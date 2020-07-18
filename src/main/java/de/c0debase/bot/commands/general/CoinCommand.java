package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.utils.DiscordUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class CoinCommand extends Command {

    public CoinCommand() {
        super("coins", "Zeigt dir deine Coins", Category.GENERAL, "money");
    }

    @Override
    public void execute(final String[] args, final Message message) {
        final Member member = DiscordUtils.getAddressedMember(message);
        final EmbedBuilder embedBuilder = getEmbed(message.getGuild(), member.getUser());
        embedBuilder
                .setDescription(member.getAsMention() + " hat "
                        + String.format("%.2f", bot.getDatabase().getUserDao()
                                .getOrCreateUser(member.getGuild().getId(), member.getUser().getId()).getCoins())
                        + " Coins");
        message.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }
}
