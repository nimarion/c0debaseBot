package de.c0debase.bot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class PingCommand extends Command {

    public PingCommand() {
        this.name = "ping";
        this.help = "Zeigt dir den Ping des Bots zu Discord";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.appendDescription(":stopwatch: " + commandEvent.getJDA().getGatewayPing());
        embedBuilder.setColor(Color.GREEN);
        commandEvent.reply(embedBuilder.build());
    }

}
