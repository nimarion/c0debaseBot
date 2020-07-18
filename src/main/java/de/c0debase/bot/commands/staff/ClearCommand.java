package de.c0debase.bot.commands.staff;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.utils.DiscordUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;

import java.util.List;

public class ClearCommand extends Command {

    public ClearCommand() {
        super("clear", "Löscht eine bestimmte Anzahl an Nachrichten", Category.STAFF);
    }

    @Override
    public void execute(final String[] args, final Message message) {
        if (args.length == 0) {
            final EmbedBuilder embedBuilder = getEmbed(message.getMember());
            embedBuilder.appendDescription("!clear <Amount>");
            message.getTextChannel().sendMessage(embedBuilder.build()).queue();
        } else {
            int i = 0;
            try {
                i = Integer.valueOf(args[0]);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
            final MessageHistory history = new MessageHistory(message.getTextChannel());
            final List<Message> messages = history.retrievePast(i + 1).complete();
            message.getTextChannel().deleteMessages(messages).queue();

            final EmbedBuilder embedBuilder = DiscordUtils.getDefaultEmbed(message.getMember());
            embedBuilder.setColor(message.getGuild().getSelfMember().getColor());
            embedBuilder.appendDescription("Es wurden **" + (i) + "** Nachrichten gelöscht");
            message.getTextChannel().sendMessage(embedBuilder.build()).queue();
        }
    }
}
