package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;

public class StackOverFlowCommand extends Command {

    private static final String STACKOVERFLOW_SEARCH_URL = "https://stackoverflow.com/?q=%s";

    public StackOverFlowCommand() {
        super("stackoverflow", "Sucht für dich auf Stackoverflow", Category.GENERAL, "sof");
    }

    public void execute(final String[] args, final Message message) {
        String url = String.format(STACKOVERFLOW_SEARCH_URL, String.join("%20", args));
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.ORANGE);
        embedBuilder.setDescription(url);
    }
}
