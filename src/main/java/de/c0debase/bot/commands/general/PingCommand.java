package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;

public class PingCommand extends Command {

    public PingCommand() {
        super("ping", "Zeigt dir den Ping des Bots zu Discord", Category.GENERAL);
    }

    @Override
    public void execute(final String[] args, final Message message) {
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.appendDescription(":stopwatch: " + message.getJDA().getPing());
        embedBuilder.setColor(Color.GREEN);
        message.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }
}
