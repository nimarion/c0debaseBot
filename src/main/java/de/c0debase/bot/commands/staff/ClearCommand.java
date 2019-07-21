package de.c0debase.bot.commands.staff;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import de.c0debase.bot.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;

import java.util.List;

public class ClearCommand extends Command {

    public ClearCommand() {
        this.name = "clear";
        this.requiredRole = "Team";
        this.guildOnly = true;
        this.help = "Löscht eine bestimmte Anzahl an Nachrichten";
        this.arguments = "[number]";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if (commandEvent.getArgs().isEmpty()) {
            final EmbedBuilder embedBuilder = EmbedUtils.getEmbed(commandEvent.getAuthor(), false);
            embedBuilder.appendDescription("!clear <Amount>");
            commandEvent.reply(embedBuilder.build());
        } else {
            int i = 0;
            try {
                i = Integer.valueOf(commandEvent.getArgs());
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
            final MessageHistory history = new MessageHistory(commandEvent.getTextChannel());
            final List<Message> messages = history.retrievePast(i + 1).complete();
            commandEvent.getTextChannel().deleteMessages(messages).queue();

            final EmbedBuilder embedBuilder = EmbedUtils.getEmbed(commandEvent.getAuthor(), true);
            embedBuilder.setDescription("Es wurden **" + (i) + "** Nachrichten gelöscht");
            commandEvent.reply(embedBuilder.build());
        }
    }
}
