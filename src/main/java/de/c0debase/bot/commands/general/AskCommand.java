package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class AskCommand extends Command {

    public AskCommand() {
        super("ask", "Zeigt dir wie man richtig Fragen stellt", Category.GENERAL);
    }

    @Override
    public void execute(String[] args, Message message) {
        final EmbedBuilder embedBuilder = getEmbed(message.getGuild(), message.getAuthor());
        embedBuilder.setTitle("Wie man Fragen richtig stellt", "https://www.dontasktoask.com/");
        embedBuilder.appendDescription("Zur schnellen Problemlösung solltest Du folgende Tipps berücksichtigen: \n\n");
        embedBuilder.appendDescription("1. Eigeninitiative. Sprich, einfach mal das Internet benutzen.\n");
        embedBuilder.appendDescription("2. Red nicht lange drum herum, komm zum Punkt.\n");
        embedBuilder.appendDescription("3. Sei nicht sparsam und gib so viele Infos wie möglich.\n");
        embedBuilder.appendDescription("4. Schick Deinen Code ruhig mit.\n");
        embedBuilder.appendDescription("5. Lies Dir nochmal Deine Nachricht durch.\n\n");
        embedBuilder.appendDescription("[Mehr zum Thema findest Du hier.](https://gist.github.com/finreinhard/b842b690255bb80d929f8a5467d75597)");

        message.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }

}