package de.c0debase.bot.commands.general;

import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class PingCommand extends Command {

    public PingCommand() {
        super("ping", "Zeigt dir den Ping des Bots zu Discord", Categorie.GENERAL);
    }

    @Override
    public void execute(String[] args, Message msg) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.appendDescription(":stopwatch: " + CodebaseBot.getInstance().getJda().getPing());
        embedBuilder.setColor(Color.GREEN);
        msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }
}
