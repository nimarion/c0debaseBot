package de.c0debase.bot.listener.message;

import de.c0debase.bot.core.Codebase;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TableFlipListener extends ListenerAdapter {
    private static final String TABLE_FLIP = "(╯°□°）╯︵ ┻━┻";
    private static final String UNFLIP = "┬─┬ ノ( ゜-゜ノ)";

    public TableFlipListener(final Codebase bot) {
        bot.getJDA().addEventListener(this);
    }

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().equals(TABLE_FLIP)) {
            event.getChannel().sendMessage(UNFLIP).queue(null, ignored -> {});
        }
    }
}
