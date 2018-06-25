package de.c0debase.bot.commands.general;

import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author Biosphere
 * @date 23.06.18
 */
public class CoinCommand extends Command {

    public CoinCommand() {
        super("coins", "Zeigt dir deine Coins", Categorie.GENERAL, "money");
    }

    @Override
    public void execute(String[] args, Message msg) {
        final Member member = msg.getMentionedMembers().isEmpty() ? msg.getMember() : msg.getMentionedMembers().get(0);
        if (member.getUser().isBot()) {
            msg.getTextChannel().sendMessage(new EmbedBuilder().setDescription(member.getAsMention() + " kann keine Coins haben").build()).queue();
            return;
        }
        EmbedBuilder embedBuilder = getEmbed(msg.getGuild(), member.getUser());
        CodebaseBot.getInstance().getMongoDataManager().getLevelUser(msg.getGuild().getId(), member.getUser().getId(), levelUser -> {
            embedBuilder.setDescription(member.getAsMention() + " hat " + String.format("%.2f", levelUser.getCoins()) + " Coins");
            msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
        });
    }
}
