package de.c0debase.bot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import de.c0debase.bot.core.Codebase;
import de.c0debase.bot.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class CoinCommand extends Command {

    private final Codebase bot;

    public CoinCommand(final Codebase instance) {
        this.bot = instance;
        this.name = "coins";
        this.help = "Zeigt dir deine Coins";
        this.guildOnly = true;
        this.aliases = new String[]{"money"};
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        final Message message = commandEvent.getMessage();
        final Member member = message.getMentionedMembers().size() == 0 ? message.getMember() : ((message.getMentionedMembers().get(0).getUser().isBot()) ? message.getMember() : message.getMentionedMembers().get(0));
        final EmbedBuilder embedBuilder = EmbedUtils.getEmbed(message.getAuthor(), true);
        embedBuilder.setDescription(member.getAsMention() + " hat " + String.format("%.2f", bot.getDataManager().getUserData(member.getGuild().getId(), member.getUser().getId()).getCoins()) + " Coins");
        commandEvent.reply(embedBuilder.build());
    }
}
